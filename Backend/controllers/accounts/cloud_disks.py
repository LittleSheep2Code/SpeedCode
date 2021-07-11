from flask import Blueprint

from common.authorization import access_require

cloud_disks = Blueprint("cloud_disks", __name__, url_prefix="/cloud-disks")

@cloud_disks.post("/commit")
@access_require(permission_require=5)               # EAS(赞助者) 权限需要
def commit_code_file():

        if request.files.get("file"):
            return {
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot load payload",
                   "reason_code": "PAYERR"
               }, 400

        request.files.get("file").stream