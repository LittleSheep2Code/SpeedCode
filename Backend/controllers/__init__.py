from controllers.connection_checker import connection_checker
from controllers.system_console import console
from controllers.run_progarm import program_executor

from controllers.accounts import *

class controller_manager:

    controllers = [
        connection_checker,
        console,

        program_executor,

        account_add_delete,
        high_authorization,
        activate_utils
    ]

    def __init__(self, application_instance):

        for controller in self.controllers:
            application_instance.register_blueprint(controller)
