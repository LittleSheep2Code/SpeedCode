import hashlib
import time

import jwt

from models.model_utils import utils
from models.account_model import Account

def summon_new_access_token(instance: Account):

    # Summon new access token
    payload = dict(exp=time.time() + 86400)
    payload.update(utils.object_as_dict(instance))

    access = jwt.encode(payload, instance.password, "HS256")

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
        if (uuid is None or data.get("uuid") == uuid) and Account.query.filter_by(access_token=access).first() is not None:
            return True

        else:
            return False


def password_process(source: str):
    data = source
    for i in range(10):
        data = hashlib.md5(data.encode("utf-8")).hexdigest()

    return data