from flask import Blueprint, request

from models.model_utils import utils
from services.program_runner import *

program_executor = Blueprint("program_executor", __name__, url_prefix="/program-runner")

@program_executor.post("")
def run_program():

    stdin = request.form.get("stdin")
    source = request.form.get("source")
    language = request.form.get("language")

    if source is None or language is None:
        return {
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot load payload",
                   "reason_code": "PAYERR"
               }, 400

    if program_runner.available_language[language] is None:
        return {
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot found require language",
                   "reason_code": "WRODAT"
               }, 400

    # 创建执行请求

    if stdin is None:
        information = program_runner.commit_submission(source, request.remote_addr, program_runner.available_language[language])

    else:
        information = program_runner.commit_submission(source, request.remote_addr, program_runner.available_language[language], stdin)

    return {
        "status": "completed",
        "information": dict(information),
        "status_code": "PASSED"
    }

@program_executor.get("/history")
def list_history():

    objective = request.args.get("sender")

    if objective is None:
        return {
                   "status": "request denied",
                   "status_code": "REQDID",
                   "reason": "cannot load payload",
                   "reason_code": "PAYERR"
               }, 400

    history_list = Execute.query.filter_by(sender=objective).all()

    for i in range(len(history_list)):
        history_list[i] = utils.object_as_dict(history_list[i])

    return {
        "status": "completed",
        "information": history_list,
        "status_code": "PASSED"
    }