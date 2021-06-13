import functools

import flask

from models.account_model import account

def access_require(permission_require=-256, permission_maximum=None, state_require=0, state_maximum=None, admin_ignore=False):
    def decorator(func):

        @functools.wraps(func)
        def wrapper(*args, **kwargs):

            print(f"(Authorization): Catch a request, processing... ip: {flask.request.remote_addr})")

            # 获取 AccessToken
            access_token = flask.request.headers.get("access_token")

            # 检查用户可用性
            # 检查 AccessToken 是否为空
            if access_token is None:
                return {
                   "status": "connection refused",
                   "reason": "please provide a correct access_token",
                   "status_code": "CONREF",
                   "reason_code": "UNDFID"
                }, 400

            # 获取用户实例
            entity = account(account.query.filter_by(access_token=access_token).first())
            if entity is None:
                return {
                    "status": "connection refused",
                    "reason": "please provide a correct access_token",
                    "status_code": "CONREF",
                    "reason_code": "UNDFID"
                }, 400

            if entity.state >= state_require and (state_maximum is not None or entity.state < state_maximum):
                if entity.permission >= permission_require and (permission_maximum is not None or (admin_ignore and entity.permission >= 155) or (not admin_ignore and entity.permission > permission_maximum)):

                    # 允许访问
                    return func(*args, **kwargs)


            return {
               "status": "connection refused",
               "reason": "permission denied",
               "status_code": "CONREF",
               "reason_code": "PERDID"
            }, 400

        return wrapper
    return decorator


@access_require
def ig():
    pass