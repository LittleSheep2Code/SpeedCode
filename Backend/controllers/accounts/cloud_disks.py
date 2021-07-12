from datetime import datetime
from flask import Blueprint, request

from common.authorization import access_require
from models import Account, CloudSource
from models.connection_factory import database

cloud_disks = Blueprint("cloud_disks", __name__, url_prefix="/cloud-disks")

@cloud_disks.post("/commit")
@access_require(permission_require=5)               # EAS(赞助者) 权限需要
def commit_code_file():

        if request.form.get("file") is None or request.form.get("address") is None:
            return {
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot load payload",
                   "reason_code": "PAYERR"
               }, 400

        file_owner = Account.query.filter_by(access_token=request.headers.get("access_token")).first()

        file_object = CloudSource.query.filter_by(address=request.form.get("address")).first()

        # 判断该路径是否占用
        if file_object is not None:
            return {
                "status": "Upload failed",
                "status_code": "FAILED",
                "reason": "duplicate file address",
                "reason_code": "REPEAT"
            }

        # 创建对象准备上传
        file_object = CloudSource()
        file_object.create_date = datetime.now()
        file_object.owner = file_owner.uuid
        file_object.content = request.form.get("file")
        file_object.address = request.form.get("address")

        database.session.add(file_object)
        database.session.commit()

        return {
            "status": "Your file was committed",
            "status_code": "PASSED",
            "information": request.form.get("address")
        }

@cloud_disks.post("/download")
@access_require(permission_require=5)
def download_code_file():

    if request.form.get("address") is None:
        return {
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot load payload",
                   "reason_code": "PAYERR"
               }, 400

    file_object = CloudSource.query.filter_by(address=request.form.get("address")).first()