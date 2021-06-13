from common.global_value_manage import global_values

database = global_values().get("database")

class execute(database.Model):
    __tablename__ = "Execute"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    stdin = database.Column(database.String)
    stdout = database.Column(database.String)
    sender = database.Column(database.String)
    source_code = database.Column(database.String)

    create_date = database.Column(database.Date)