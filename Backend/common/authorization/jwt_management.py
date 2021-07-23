import datetime
import hashlib

import jwt

from models.account_model import Account
from models.connection_factory import database


def summon_new_access_token(instance: Account):

    # Summon new access token
    payload = dict(exp=datetime.datetime.utcnow() + datetime.timedelta(days=7), flag=1, iss="speedcode", iat=datetime.datetime.utcnow())
    payload.update({"uuid": instance.uuid, "username": instance.username})

    access = jwt.encode(payload=payload, key=instance.password, algorithm="HS256")

    # Commit
    Account.query.filter_by(uuid=instance.uuid).update({ "access_token": access })
    database.session.commit()

    # Return
    return access

def decode_exists_access_token(access: str, key: str):
    try:
        payload = jwt.decode(access, key=key, algorithms="HS256")

    except (jwt.ExpiredSignatureError, jwt.InvalidTokenError, jwt.InvalidSignatureError):
        return ""

    else:
        return payload

def check_exists_access_token(access: str, entity_data):

    if access:
        payload = decode_exists_access_token(access, entity_data.password)

        if not payload:
            return False

        if "username" in payload and "uuid" in payload:
            return True

    return False


def password_process(source: str):
    data = source
    for i in range(10):
        data = hashlib.md5(data.encode("utf-8")).hexdigest()

    return data