from flask_sqlalchemy import inspect

class utils(object):

    @staticmethod
    def object_as_dict(obj):
        return {c.key: getattr(obj, c.key)
                for c in inspect(obj).mapper.column_attrs}