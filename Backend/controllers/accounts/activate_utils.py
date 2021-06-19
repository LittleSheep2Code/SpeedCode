from uuid import uuid1
from datetime import datetime
from flask import Blueprint, request

from common.authorization import access_require
from models.connection_factory import database
from models import Account, Activate

activate_utils = Blueprint("activate_utils", __name__, url_prefix="/account/activate")

@activate_utils.post("")
@access_require(state_require=0, state_maximum=1)
def activate_account():
    activate_code = request.form.get("source")
    activate_entity = Activate.query.filter_by(source=activate_code).first()

    if activate_code is None:
        return {
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot load payload",
                   "reason_code": "PAYERR"
               }, 400

    if activate_entity is None:
        return {
            "status": "request denied",
            "status_code": "REQDID",
            "reason": "cannot found source activate code",
            "reason_code": "WRODAT"
        }

    Activate.query.filter_by(source=activate_code).update({"amount": activate_entity.amount - 1})
    Account.query.filter_by(access_token=request.headers.get("access_token")).update({"state": 1})
    database.session.commit()

    return {
        "status": "completed",
        "status_code": "PASSED"
    }

@activate_utils.post("/add")
@access_require(permission_require=155)
def add_activate_code():
    activate_code = request.form.get("source")
    amount = request.form.get("amount")

    if activate_code is None:
        activate_code = str(uuid1())

    if amount is None:
        amount = 1

    if activate_code is None and Activate.query.filter_by(source=activate_code).first() is not None:
        return {
            "status": "request denied",
            "status_code": "REQDID",
            "reason": "same activate code",
            "reason_code": "WRODAT"
        }

    activate_entity = Activate(source=activate_code, amount=amount, create_date=datetime.now())
    database.session.add(activate_entity)
    database.session.commit()

    return {
        "status": "completed",
        "information": activate_code,
        "status_code": "PASSED"
    }