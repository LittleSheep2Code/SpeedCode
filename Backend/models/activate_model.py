from models.connection_factory import database

class Activate(database.Model):
    __tablename__ = "Activate"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    adder = database.Column(database.String)
    source = database.Column(database.String)
    settings = database.Column(database.String)

    destroy_date = database.Column(database.DateTime)
    create_date = database.Column(database.DateTime)

    amount = database.Column(database.Integer)
    state = database.Column(database.Integer)
