import hashlib
import time

import jwt

from models.account_model import account

def summon_new_access_token(instance: account):

    # Summon new access token
    payload = dict(dict(account.__dict__) + {"exp": time.time() + 86400})
    access = jwt.encode(payload, account.password, "HS256")

    # Commit
    instance.update({ "access_token": access })

    # Return
    return access

def verify_access_token(access: str, uuid: str=None):
    data = {}

    # Decode access token and verify
    try:
        data = jwt.decode(access, uuid, True, algorithm='HS256')

    # Access token wrong or timeout
    except Exception:
        return False

    # Access token is right
    finally:
        if (uuid is None or data.get("uuid") == uuid) and account.query.filter_by(access_token=access).first() is not None:
            return True

        else:
            return False


def password_process(source: str):
    data = source
    for i in range(10):
        data = hashlib.md5(data).hexdigest()

    return data