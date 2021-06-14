from flask import Flask
from flask_mail import Message

from common.global_value_manage import global_values

def send_email(application_instance: Flask, content: Message):
    with application_instance.app_context():
        global_values().get("mail").send(content)