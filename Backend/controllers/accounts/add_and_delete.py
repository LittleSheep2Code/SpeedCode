import datetime
import re
from uuid import uuid1

from flask import Blueprint, request, render_template

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
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot load payload",
                   "reason_code": "PAYERR"
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
                   "status": "sign in failed",
                   "status_code": "FAILED",
                   "reason": "wrong data",
                   "reason_code": "WRODAT"
               }, 201

    # 登陆成功
    # 更新 AccessToken
    if not verify_access_token(entity.access_token, entity.uuid):

        # 需要更新
        summon_new_access_token(entity)

    # 返回 AccessToken 以及登陆成功
    return {
        "status": "sign in success",
        "information": entity.access_token,
        "status_code": "PASSED"
    }


@account_add_delete.post("/sign-up")
def register():
    username = request.form.get("username")
    password = request.form.get("password")
    email = request.form.get("email")
    email_code = request.form.get("email_code")

    if username is None or password is None or email is None:
        return {
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot load payload",
                   "reason_code": "PAYERR"
               }, 400

    # 第一次请求
    if email_code is None:

        # 注册条件
        if not re.match("^[a-zA-Z][a-zA-Z0-9_]{4,32}$", username):
            return {
                "status": "request denied",
                "status_code": "REQDID",
                "reason": "illegal username, please use letters, number and underline characters",
                "reason_code": "WRODAT-NAME"
            }

        if not re.match("^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$", email):
            return {
                "status": "request denied",
                "status_code": "REQDID",
                "reason": "illegal email, please check your email",
                "reason_code": "WRODAT-NAME"
            }

        if Account.query.filter_by(username=username).first() is not None:
            return {
                "status": "request denied",
                "status_code": "REQDID",
                "reason": "same username",
                "reason_code": "REPEAT-NAME"
            }

        if len(Account.query.filter_by(email=email).all()) >= 2:
            return {
                "status": "request denied",
                "status_code": "REQDID",
                "reason": "one email has more than two Account alert",
                "reason_code": "REPEAT-EMAIL"
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
            "status": "completed sent verify mail. please view your mailbox",
            "information": "done",
            "status_code": "PASSED"
        }

    # 第二次请求
    else:

        entity = Account.query.filter_by(username=username).first()
        if entity is None:
            return {
                "status": "please send first request or verify your data",
                "status_code": "WRODAT"
            }

        if entity.mail_access_code == email_code:
            Account.query.filter_by(username=username) \
                .update({"state": 0, "destroy_date": datetime.datetime.now() + datetime.timedelta(days=30), "mail_access_code": None})
            database.session.commit()

            return {
                "status": "completed",
                "status_code": "PASSED"
            }

        return {
            "status": "please verity your data",
            "status_code": "WRODAT",
        }