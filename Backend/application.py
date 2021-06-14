from flask import Flask
from flask_mail import Mail
from flask_sqlalchemy import SQLAlchemy

from common.global_value_manage import global_values
from enctryption_config import EMAIL_PASSWORD

application = Flask(__name__, template_folder="resource/templates")

# Application configure
application.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///resource/source.sqlite3"
application.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = True

application.config["MAIL_USERNAME"] = "smartsheep.codecraft@outlook.com"
application.config["MAIL_SERVER"] = "outlook.office365.com"
application.config["MAIL_PASSWORD"] = EMAIL_PASSWORD
application.config["MAIL_USE_TLS"] = True
application.config["MAIL_PORT"] = 993

# Mail sender
global_values().set("mail", Mail(application))

# Database
global_values().set("database", SQLAlchemy(application, session_options={ "autocommit": True }))

# Import models
from models import *

# Boot
if __name__ == '__main__':

    # Init router
    from controllers import controller_manager
    controller_manager(application)

    application.run(port=20020)
