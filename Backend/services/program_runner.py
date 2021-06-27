from time import sleep

from requests import post, get
from datetime import datetime
from enum import Enum

from enctryption_config import JUDGE0_API
from models import Execute
from models.connection_factory import database


class program_runner(object):

    class available_language(Enum):
        NASM = 45
        BASH = 46
        BASIC = 47

        C_CLANG_7 = 75
        C_GCC_7 = 48
        C_GCC_8 = 49
        C_GCC_9 = 50

        C_SHARP = 51

        CPP_CLANG_7 = 76
        CPP_GPP_7 = 52
        CPP_GPP_8 = 53
        CPP_GPP_9 = 54

        GOLANG = 60
        JAVA = 62
        NODE_JS = 63
        KOTLIN = 78
        LUA = 64
        OBJECTIVE_C = 79
        PASCAL = 67
        PERL = 85
        PHP = 68

        PYTHON_2 = 70
        PYTHON_3 = 71

        R = 80
        RUBY = 72
        RUST = 73
        SCALA = 81
        SWIFT = 83
        VB_NET = 84
        TYPESCRIPT = 74

    @staticmethod
    def commit_submission(source, sender, language: available_language, stdin="", auto_select=True):
        parma = {
            "stdin": stdin,
            "source_code": source,
            "language_id": language.value
        }

        response = post(JUDGE0_API + "/submissions", parma)

        if response.status_code != 201:
            return None

        # Query result
        result_json = response.json()
        result = program_runner.select_submission(result_json["token"])

        # Add history
        entity = Execute(stdin=stdin, stdout=result["stdout"], sender=sender, source_code=source, create_date=datetime.now())

        database.session.add(entity)
        database.session.commit()

        if auto_select:
            return result

        else:
            return response.json()["token"]

    @staticmethod
    def select_submission(mission_id):

        while True:
            sleep(0.5)
            response = get(JUDGE0_API + "/submissions/" + mission_id)
            response_json = response.json()

            if response_json["status"]["id"] != 1 and response_json["status"]["id"] != 2:
                break

        return response_json