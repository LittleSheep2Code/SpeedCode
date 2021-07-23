from flask import Blueprint, request

import platform

connection_checker = Blueprint("connection_checker", __name__)

@connection_checker.route("/")
def check():
    return {
        "status": "OK",
        "status_code": "200",
        "return": {
            "client": {
                "addr": request.remote_addr,
                "user": request.remote_user
            },

            "server": {
                "arch": platform.architecture(),
                "system": platform.system()
            }
        }
    }