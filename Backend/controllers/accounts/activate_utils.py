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

    if activate_code is None:
        return {
                   "status": "Rejected",
                   "status_code": "401",
                   "reason": "Cannot load payload",
               }, 400

    activate_entity = Activate.query.filter_by(source=activate_code).first()

    if activate_entity is None:
        return {
            "status": "Cannot found source activate code",
            "status_code": "404"
        }

    Activate.query.filter_by(source=activate_code).update({"amount": activate_entity.amount - 1})
    Account.query.filter_by(access_token=request.headers.get("access_token")).update({"state": 1})
    database.session.commit()

    return {
        "status": "OK",
        "status_code": "200"
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
            "status": "Rejected",
            "status_code": "401",
            "reason": "Same activate code",
        }

    activate_entity = Activate(source=activate_code, amount=amount, create_date=datetime.now())
    database.session.add(activate_entity)
    database.session.commit()

    return {
        "status": "OK",
        "return": activate_code,
        "status_code": "200"
    }