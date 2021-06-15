from os import environ
from flask import Flask
from flask_mail import Mail
from flask_apscheduler import APScheduler

from models.connection_factory import database
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

# Init scheduler
from scheduler import scheduler
from scheduler.tasks_configures import scheduler_config

application.config.from_object(scheduler_config)

# Boot
if __name__ == '__main__':

    # Init database
    database.init_app(application)

    # Init router
    from controllers import controller_manager
    controller_manager(application)

    # Start scheduler
    scheduler.init_app(application)
    scheduler.start()

    application.run(port=20020)
