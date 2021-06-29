from flask import Blueprint, request

from enctryption_config import CONSOLE_KEY
from models.connection_factory import database

console = Blueprint("console", __name__, url_prefix="/console")

### ACCESS CONFIGURE ### DANGER ### ACCESS CONFIGURE ###
final_code = CONSOLE_KEY
### ACCESS CONFIGURE ### DANGER ### ACCESS CONFIGURE ###

@console.route("/init")
def init():
    if request.headers.get("final_access_code") != final_code:
        return {
            "status": "failed",
            "information": "wrong final access code",
            "status_code": "PERDID"
        }

    else:
        database.create_all()

        return {
            "status": "completed",
            "information": "init completed",
            "status_code": "PASSED"
        }

@console.route("/drop")
def drop():
    if request.headers.get("final_access_code") != final_code:
        return {
            "status": "failed",
            "information": "wrong final access code",
            "status_code": "PERDID"
        }

    else:
        database.drop_all()

        return {
            "status": "completed",
            "information": "drop completed",
            "status_code": "PASSED"
        }