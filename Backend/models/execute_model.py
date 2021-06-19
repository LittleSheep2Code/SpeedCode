from models.connection_factory import database

class Execute(database.Model):
    __tablename__ = "Execute"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    stdin = database.Column(database.String)
    stdout = database.Column(database.String)
    sender = database.Column(database.String)
    source_code = database.Column(database.String)

    create_date = database.Column(database.DateTime)