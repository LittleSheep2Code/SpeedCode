from controllers.connection_checker import connection_checker
from controllers.system_console import console

class controller_manager:

    controllers = [
        connection_checker,
        console
    ]

    def __init__(self, application_instance):

        for controller in self.controllers:
            application_instance.register_blueprint(controller)
