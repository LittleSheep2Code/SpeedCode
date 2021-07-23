from uuid import uuid1

from flask import Blueprint, request, render_template

from common.authorization import access_require
from common.email_sender import send_email
from models.account_model import Account
from models.connection_factory import database

high_authorization = Blueprint("high_authorization", __name__, url_prefix="/Account")

def summon_email_authorization_code(uuid: str):
    email_code = str(uuid1())[:6]

    entity = Account.query.filter_by(uuid=uuid).first()
    if entity is not None:
        Account.query.filter_by(uuid=uuid).update({"mail_access_code": email_code})
        database.session.commit()

    return email_code

def verify_email_authorization_code(uuid: str, email_code: str):

    entity = Account.query.filter_by(uuid=uuid, email_access_code=email_code).first()
    if entity is None:
        return False

    return True

@high_authorization.route("/high-authorization")
@access_require()
def authorization():
    entity = Account.query.filter_by(access_token=request.headers.get("access_token")).first()

    send_email(render_template("mail/register-email-code.html").replace("${EMAIL_CODE}", summon_email_authorization_code(entity.uuid)).replace("${USERNAME}", entity.username),
               entity.email,
               "SpeedCode email verify")

    return {
        "status": "OK",
        "status_code": "200"
    }