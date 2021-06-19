from flask_sqlalchemy import inspect

class utils(object):

    @staticmethod
    def object_as_dict(source):
        output = {}

        for column in source.__table__.columns:
            output[column.name] = str(getattr(source, column.name))

        return output