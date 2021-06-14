from common.global_value_manage import global_values

database = global_values().get("database")

class account(database.Model):
    __tablename__ = "Account"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    uuid = database.Column(database.String)
    email = database.Column(database.String)
    username = database.Column(database.String)
    password = database.Column(database.String)

    access_token = database.Column(database.String)
    mail_access_code = database.Column(database.String)

    destroy_date = database.Column(database.Date)
    create_date = database.Column(database.Date)

    permission = database.Column(database.Integer)
    state = database.Column(database.Integer)
