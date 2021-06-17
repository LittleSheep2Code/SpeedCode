from datetime import datetime

from scheduler import scheduler
from models.connection_factory import database
from models.account_model import Account
from models.activate_model import Activate

def model_cleaner():

    with scheduler.app.app_context():

        delete_accounts = Account.query.filter((Account.destroy_date <= datetime.now()) | (Account.state < 2))
        for account in delete_accounts:
            print(f"[MODEL_EVENT] => {datetime.now().date()} Cleaning model Account with username: {account.username}")
            database.session.delete(account)

        delete_activates = Activate.query.filter(Activate.destroy_date <= datetime.now())
        for activate in delete_activates:
            print(f"[MODEL_EVENT] => {datetime.now().date()} Cleaning model Activate with source: {activate.source}")
            database.session.delete(activate)