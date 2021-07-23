from flask import Blueprint, request

from encryption_config import CONSOLE_KEY
from models.connection_factory import database

console = Blueprint("console", __name__, url_prefix="/console")

final_code = CONSOLE_KEY

@console.put("/init")
def init():
    if request.headers.get("final_access_code") != final_code:
        return {
            "status": "Error",
            "return": "Wrong final access code",
            "status_code": "400"
        }

    else:
        database.create_all()

        return {
            "status": "OK",
            "return": "Init completed",
            "status_code": "200"
        }

@console.put("/drop")
def drop():
    if request.headers.get("final_access_code") != final_code:
        return {
            "status": "Exited",
            "return": "Wrong final access code",
            "status_code": "400"
        }

    else:
        database.drop_all()

        return {
            "status": "OK",
            "return": "Drop completed",
            "status_code": "200"
        }