import datetime
import functools

import flask

from models.account_model import Account

def access_require(permission_require=-256, permission_maximum=None, state_require=0, state_maximum=None, admin_ignore=False):
    def decorator(func):

        @functools.wraps(func)
        def wrapper(*args, **kwargs):

            print(f"[{datetime.datetime.now()}] (Authorization): Catch a request, processing... ip: {flask.request.remote_addr}")

            # 获取 AccessToken
            access_token = flask.request.headers.get("access_token")

            # 检查用户可用性
            # 检查 AccessToken 是否为空
            if access_token is None:
                return {
                   "status": "Rejected",
                   "reason": "Please provide a correct access_token",
                   "status_code": "433",
                }, 403

            # 获取用户实例
            entity = Account.query.filter_by(access_token=access_token).first()
            if entity is None:
                return {
                    "status": "Rejected",
                    "reason": "Please provide a correct access_token",
                    "status_code": "433",
                }, 403

            if entity.state >= state_require and (state_maximum is None or entity.state < state_maximum):
                if entity.permission >= permission_require and (permission_maximum is None or (admin_ignore and entity.permission >= 155) or (not admin_ignore and entity.permission > permission_maximum)):

                    # 允许访问
                    return func(*args, **kwargs)


            return {
               "status": "Rejected",
               "reason": "Permission denied or exceed the maximum",
               "status_code": "433",
               "ultra_code": "ml"
            }, 403

        return wrapper
    return decorator


@access_require
def ig():
    pass