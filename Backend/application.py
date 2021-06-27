from flask import Flask

from models.connection_factory import database

application = Flask(__name__, template_folder="resource/templates")

# Application configure
application.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///resource/source.sqlite3"
application.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = True
application.config["CONFIRM_DELETED_ROWS"] = False

# Init scheduler
from scheduler import scheduler
from scheduler.tasks_configures import scheduler_config

application.config.from_object(scheduler_config)

# Init database
database.init_app(application)

# Init router
from controllers import controller_manager
controller_manager(application)

# Start scheduler
scheduler.init_app(application)
scheduler.start()

# Boot
if __name__ == '__main__':

    application.run(host="0.0.0.0", port=20020, debug=True)
