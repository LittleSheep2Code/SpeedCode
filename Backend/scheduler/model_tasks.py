from datetime import datetime

from scheduler import scheduler
from models.connection_factory import database
from models.account_model import Account
from models.activate_model import Activate

def model_cleaner():

    with scheduler.app.app_context():

        print(f"[MODEL_EVENT] => {datetime.now().date()} Cleaning models...")

        delete_accounts = Account.query.filter(Account.destroy_date >= datetime.now())
        for account in delete_accounts:
            database.session.delete(account)

        delete_activates = Activate.query.filter(Activate.destroy_date >= datetime.now())
        for activate in delete_activates:
            database.session.delete(activate)

        print(f"[MODEL_EVENT] => {datetime.now().date()} Clean task completed")