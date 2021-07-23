import datetime

from requests import post

from encryption_config import EMAIL_API_KEYS
from common.global_value_manage import global_values
from scheduler import scheduler

def send_email(content: str, receive: str, subject: str, sender: str= "SpeedCode Official(norely) <no-reply@mail.speed-code.online>"):
    return post(
        "https://api.hedwi.com/mail/mail.speed-code.online",
        auth=("api", EMAIL_API_KEYS),
        data={
            "from": sender,
            "to": receive,
            "subject": subject,
            "html": content})