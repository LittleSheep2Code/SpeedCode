from flask import Flask
from flask_sqlalchemy import SQLAlchemy

from common.global_value_manage import global_values

application = Flask(__name__)

# Application configure
application.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///resource/source.sqlite3"
application.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = True

# Database
global_values().set("database", SQLAlchemy(application))

# Import models
from models import *

# Boot
if __name__ == '__main__':

    # Init router
    from controllers import controller_manager
    controller_manager(application)

    application.run(port=20020)
