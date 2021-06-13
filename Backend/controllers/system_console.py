from flask import Blueprint, request

from common.global_value_manage import global_values
from common.authorization import access_require

database = global_values().get("database")
console = Blueprint("console", __name__, url_prefix="/console")

### ACCESS CONFIGURE ### DANGER ### ACCESS CONFIGURE ###
final_code = "ILUULMMXBCTMM@LJD8D"
### ACCESS CONFIGURE ### DANGER ### ACCESS CONFIGURE ###

@console.route("/init")
@access_require()
def reinstall():
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
            "information": "reinstall completed, please restart server",
            "status_code": "PASSED"
        }