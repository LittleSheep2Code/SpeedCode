from models.connection_factory import database

class Account(database.Model):
    __tablename__ = "Account"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    uuid = database.Column(database.String)
    email = database.Column(database.String)
    username = database.Column(database.String)
    password = database.Column(database.String)

    access_token = database.Column(database.String)
    mail_access_code = database.Column(database.String)

    destroy_date = database.Column(database.DateTime)
    create_date = database.Column(database.DateTime)

    permission = database.Column(database.Integer)
    state = database.Column(database.Integer)