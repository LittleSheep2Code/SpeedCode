from flask import Blueprint, request, render_template
from flask_mail import Message
from threading import Thread
from uuid import uuid1

from common.email_sender import send_email
from common.authorization import access_require
from models.account_model import account

high_authorization = Blueprint("high_authorization", __name__, url_prefix="/account")

def summon_email_authorization_code(uuid: str):
    email_code = str(uuid1())[:6]

    entity = account.query.filter_by(uuid=uuid).first()
    if entity is not None:
        entity.update({ "mail_access_code": email_code })

    return email_code

def verify_email_authorization_code(uuid: str, email_code: str):

    entity = account.query.filter_by(uuid=uuid, email_access_code=email_code).first()
    if entity is None:
        return False

    return True

@high_authorization.route("/high-authorization")
@access_require()
def authorization():
    entity = account(account.query.filter_by(access_token=request.headers.get("access_token")).first())

    email_message = Message("SpeedCode access request code", sender="<CodeCraft Official> smartsheep.codecraft@outlook.com", recipients=[entity])
    email_message.html = render_template("mail/register-email-code.html").replace("EMAIL_CODE", summon_email_authorization_code(entity.uuid)).replace("USERNAME", entity.username)
    Thread(target=send_email, args=[high_authorization, email_message]).start()

    return {
        "status": "completed authorization. please view your mailbox",
        "information": "done",
        "status_code": "PASSED"
    }