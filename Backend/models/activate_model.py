from models.connection_factory import database

class Activate(database.Model):
    __tablename__ = "Activate"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    source = database.Column(database.String)

    create_date = database.Column(database.DateTime)

    amount = database.Column(database.Integer)
