import hashlib
import time

import jwt

from models.account_model import Account
from models.connection_factory import database


def summon_new_access_token(instance: Account):

    # Summon new access token
    payload = dict(exp=time.time() + 172800)
    payload.update({"uuid": instance.uuid, "username": instance.username})

    access = jwt.encode(payload=payload, key=instance.password, algorithm="HS256")

    # Commit
    Account.query.filter_by(uuid=instance.uuid).update({ "access_token": access })
    database.session.commit()

    # Return
    return access

def verify_access_token(access: str, entity_data):
    data = {}

    # Decode access token and verify
    try:
        data = jwt.decode(jwt=access, key=entity_data.password, algorithms='HS256')

    # Access token wrong or timeout
    except Exception as e:
        return False

    # Access token is right
    finally:
        if (entity_data.uuid is None or data.get("uuid") == entity_data.uuid) and Account.query.filter_by(uuid=data.get("uuid")).first() is not None:

            entity = Account.query.filter_by(uuid=data.get("uuid")).first()

            if entity.access_token != access:
                return False

        return True


def password_process(source: str):
    data = source
    for i in range(10):
        data = hashlib.md5(data.encode("utf-8")).hexdigest()

    return data