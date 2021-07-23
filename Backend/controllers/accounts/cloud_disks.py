import uuid
from datetime import datetime
from flask import Blueprint, request

from common.authorization import access_require
from models import Account, CloudSource
from models.connection_factory import database

cloud_disks = Blueprint("cloud_disks", __name__, url_prefix="/cloud-disks")

# Upload a code state
@cloud_disks.post("/commit")
@access_require(permission_require=5)  # Sponsor permission require
def commit_code_state():
    if request.form.get("state") is None:
        return {
                   "status": "Rejected",
                   "status_code": "401",
                   "reason": "Cannot load payload",
               }, 400

    file_owner = Account.query.filter_by(access_token=request.headers.get("access_token")).first()

    # 用户保存历史数量是否达到上限
    if len(CloudSource.query.filter_by(owner=file_owner.uuid).all()) > 5:
        return {
            "status": "Upload failed",
            "status_code": "410",
            "reason": "History amount is more then 5",
        }

    # 创建对象准备上传
    file_object = CloudSource()
    file_object.update_date = datetime.now()
    file_object.owner = file_owner.uuid
    file_object.index = str(uuid.uuid4())
    file_object.state_content = request.form.get("state")

    database.session.add(file_object)
    database.session.commit()

    return {
        "status": "OK",
        "status_code": "200",
        "return": file_object.id
    }


# Download your code state
@cloud_disks.post("/")
@access_require(permission_require=5)
def download_code_state():
    if request.form.get("index") is None:
        return {
                   "status": "Rejected",
                   "status_code": "401",
                   "reason": "Cannot load payload",
               }, 400

    file_object = CloudSource.query.filter_by(index=request.form.get("index")).first()

    if file_object is None:
        return {
                   "status": "Error",
                   "status_code": "404"
               }, 400

    return {
        "status": "OK",
        "status_code": "200",
        "return": file_object.state_content
    }


# List your all code state
@cloud_disks.post("/list")
@access_require(permission_require=5)
def download_code_state_history():
    file_owner = Account.query.filter_by(access_token=request.headers.get("access_token")).first()
    files = CloudSource.query.filter_by(owner=file_owner.uuid).all()

    return {
        "status": "OK",
        "status_code": "200",
        "return": files
    }


# Remove(Delete) a code state
@cloud_disks.post("/remove")
@access_require(permission_require=5)
def remove_code_state():
    if request.form.get("index") is None:
        return {
                   "status": "Rejected",
                   "status_code": "401",
                   "reason": "Cannot load payload",
               }, 400

    file_object = CloudSource.query.filter_by(index=request.form.get("index")).first()

    if file_object is None:
        return {
                   "status": "Error",
                   "status_code": "404"
               }, 400

    database.session.remove(file_object)
    database.session.commit()

    return {
        "status": "OK",
        "status_code": "200",
    }


# Merge(Update) local code state to cloud code state
@cloud_disks.post("/merge")
@access_require(permission_require=5)
def merge_code_state():
    if request.form.get("index") is None or request.form.get("state") is None:
        return {
                   "status": "Rejected",
                   "status_code": "401",
                   "reason": "cannot load payload",
               }, 400

    file_object = CloudSource.query.filter_by(index=request.form.get("index")).first()

    if file_object is None:
        return {
                   "status": "Error",
                   "status_code": "404"
               }, 400

    CloudSource.query.filter_by(index=request.form.get("index")).update({"state_content": request.form.get("state")})


