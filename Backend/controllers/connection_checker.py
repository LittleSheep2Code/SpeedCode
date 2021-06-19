from flask import Blueprint

import platform

connection_checker = Blueprint("connection_checker", __name__)

@connection_checker.route("/")
def check():
    return {
        "status": "connected",
        "server": {
            "arch": platform.architecture(),
            "system": platform.system()
        }
    }