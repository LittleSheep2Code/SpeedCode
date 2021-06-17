from flask_sqlalchemy import SQLAlchemy, BaseQuery

class Query(BaseQuery):
    def as_dict(self):
        context = self._compile_context()
        context.statement.use_labels = False
        columns = [column.name for column in context.statement.columns]

        return list(map(lambda row: dict(zip(columns, row)), self.all()))

database: SQLAlchemy = SQLAlchemy(session_options={ "autocommit": True }, query_class=Query)