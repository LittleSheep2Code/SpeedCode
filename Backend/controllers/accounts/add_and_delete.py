import datetime
import re
from uuid import uuid1

from flask import Blueprint, request, render_template, Response
from io import BytesIO
from PIL import Image

from common.authorization import access_require
from common.authorization.jwt_management import *
from common.email_sender import send_email
from controllers.accounts.high_permission_authization import summon_email_authorization_code
from models.connection_factory import database

account_add_delete = Blueprint("account_add_delete", __name__, url_prefix="/account")

@account_add_delete.post("/sign-in")
def sign_in():
    username = request.form.get("username")
    password = request.form.get("password")

    if username is None or password is None:
        return {
                   "status": "Rejected",
                   "status_code": "401",
                   "reason": "Cannot load payload",
               }, 400

    p_password = password_process(password)


    if re.match("^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$", username):

        # 处理邮箱登陆
        entity = Account.query.filter_by(email=username).first()

    else:

        # 处理用户名登陆
        entity = Account.query.filter_by(username=username).first()

    # 无法从数据库读取到目标用户
    if entity is None or entity.password != p_password:
        return {
                   "status": "Error",
                   "status_code": "400",
                   "reason": "Wrong data",
               }, 201

    # 登陆成功
    # 更新 AccessToken
    if not check_exists_access_token(entity.access_token, entity) or entity.access_token == "" or entity.access_token is None:

        # 需要更新
        access = summon_new_access_token(entity)

        return {
            "status": "OK",
            "return": access,
            "status_code": "200"
        }

    # 返回 AccessToken 以及登陆成功
    return {
        "status": "OK",
        "return": entity.access_token,
        "status_code": "200"
    }


@account_add_delete.post("/sign-up")
def register():
    username = request.form.get("username")
    password = request.form.get("password")
    email = request.form.get("email")
    email_code = request.form.get("email_code")

    if username is None or password is None or email is None:
        return {
                   "status": "Rejected",
                   "status_code": "401",
                   "reason": "Cannot load payload",
               }, 400

    # 第一次请求
    if email_code is None:

        # 注册条件
        if not re.match("^[a-zA-Z][a-zA-Z0-9_]{4,32}$", username):
            return {
                "status": "Rejected",
                "status_code": "400",
                "ultra_code": "iu",
                "reason": "Illegal username, please use letters, number and underline characters",
            }

        if not re.match("^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$", email):
            return {
                "status": "Rejected",
                "status_code": "400",
                "ultra_code": "ie",
                "reason": "Illegal email, please check your email",
            }

        if Account.query.filter_by(username=username).first() is not None:
            if Account.query.filter_by(username=username).first().state == -1:
                database.session.delete(Account.query.filter_by(username=username).first())
                database.session.commit()

            else:
                return {
                    "status": "Rejected",
                    "status_code": "403",
                    "reason": "Same username",
                }

        if len(Account.query.filter_by(email=email).all()) >= 2:
            return {
                "status": "Rejected",
                "status_code": "410",
                "reason": "One email has more than two Account alert",
            }

        # 创建临时账户
        entity = Account()
        entity.uuid = str(uuid1())
        entity.state = -1
        entity.permission = 0
        entity.email = email
        entity.username = username
        entity.password = password_process(password)
        entity.create_date = datetime.datetime.now()
        entity.destroy_date = datetime.datetime.now() + datetime.timedelta(hours=2)

        # 提交至数据库
        database.session.add(entity)
        database.session.commit()

        send_email(render_template("mail/register-email-code.html").replace("${EMAIL_CODE}", summon_email_authorization_code(entity.uuid)).replace("${USERNAME}", username),
                   email,
                   "SpeedCode access request code")

        return {
            "status": "OK",
            "ultra_code": "ag",
            "status_code": "200"
        }

    # 第二次请求
    else:

        entity = Account.query.filter_by(username=username).first()
        if entity is None:
            return {
                "status": "Error",
                "status_code": "400"
            }

        if entity.mail_access_code == email_code:
            Account.query.filter_by(username=username) \
                .update({"state": 0, "destroy_date": datetime.datetime.now() + datetime.timedelta(days=30), "mail_access_code": None})
            database.session.commit()

            return {
                "status": "OK",
                "status_code": "200"
            }

        return {
            "status": "Error",
            "status_code": "400",
        }

@account_add_delete.get("/detail")
def get_account_detail():

    objective = request.args.get("where")

    if request.headers.get("access_token") is not None \
        and Account.query.filter_by(access_token=request.headers.get("access_token")).first() is not None \
        and objective is None:

        entity = Account.query.filter_by(access_token=request.headers.get("access_token")).first()

        if entity.state <= 0:
            return {
                "status": "OK",
                "return": {
                    "access_token": entity.access_token,
                    "destroy_date": entity.destroy_date,
                    "create_date": entity.create_date,
                    "permission": entity.permission,
                    "username": entity.username,
                    "password": entity.password,
                    "email": entity.email,
                    "state": entity.state,
                    "uuid": entity.uuid,
                },

                "status_code": "200"
            }

        else:
            return {
                "status": "OK",
                "return": {
                    "access_token": entity.access_token,
                    "create_date": entity.create_date,
                    "permission": entity.permission,
                    "username": entity.username,
                    "password": entity.password,
                    "email": entity.email,
                    "state": entity.state,
                    "uuid": entity.uuid,
                },

                "status_code": "200"
            }

    elif objective is not None and Account.query.filter_by(uuid=objective).first() is not None:
        entity = Account.query.filter_by(uuid=objective).first()

        return {
            "status": "OK",
            "return": {
                "create_date": entity.create_date,
                "permission": entity.permission,
                "username": entity.username,
                "email": entity.email,
                "state": entity.state,
                "uuid": entity.uuid,
            },

            "status_code": "200"
        }

    return {
        "status": "Error",
        "status_code": "404",
        "reason": "Cannot get objective user entity",
    }

@account_add_delete.get("/avatar")
@access_require()
def get_account_avatar():

    entity = Account.query.filter_by(access_token=request.headers.get("access_token")).first()
    if entity.avatar is None:
        return {
            "status": "OK",
            "return": entity.username[0],
            "status_code": "204"
        }

    else:
        image = BytesIO()
        Image.open(BytesIO(entity.avatar)).save(image, format("PNG"))

        return Response(image, mimetype="image/jpeg")

@account_add_delete.post("/avatar/set")
@access_require()
def set_account_avatar():

    image = request.files.get("file")
    if image is None:
        return {
                   "status": "Rejected",
                   "status_code": "401",
                   "reason": "Cannot load payload, require a image payload",
               }, 400

    Account.query.filter_by(access_token=request.headers.get("access_token")).update({"avatar": image.stream})
    database.session.commit()

