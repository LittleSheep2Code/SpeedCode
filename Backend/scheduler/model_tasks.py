from datetime import datetime
from flask import render_template

from scheduler import scheduler
from models.account_model import Account
from common.email_sender import send_email
from models.activate_model import Activate
from models.connection_factory import database

def model_cleaner():

    with scheduler.app.app_context():

        delete_accounts = Account.query.filter(datetime.now() > Account.destroy_date).filter(Account.state < 1).all()
        for account in delete_accounts:
            print(f"[MODEL_EVENT] => {datetime.now().date()} Cleaning model Account with username: {account.username}")
            if account.state == -1:
                send_email(render_template("mail/delete-user-reason.html").replace("${USERNAME}", account.username).replace("${DELETE_REASON}", "Your email has not verity"),
                           account.email,
                           "Your account has been deleted")

            if account.state == 0:
                send_email(
                    render_template("mail/delete-user-reason.html").replace("${USERNAME}", account.username).replace("${DELETE_REASON}", "Your account has not verity"),
                    account.email,
                    "Your account has been deleted")

            database.session.delete(account)

        delete_activates = Activate.query.filter(Activate.amount <= 0).all()
        for activate in delete_activates:
            print(f"[MODEL_EVENT] => {datetime.now().date()} Cleaning model Activate with source: {activate.source}")
            database.session.delete(activate)

        database.session.commit()