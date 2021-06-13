from common.global_value_manage import global_values

database = global_values().get("database")

class activate(database.Model):
    __tablename__ = "Activate"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    adder = database.Column(database.String)
    source = database.Column(database.String)
    settings = database.Column(database.String)

    destroy_date = database.Column(database.Date)
    create_date = database.Column(database.Date)

    amount = database.Column(database.Integer)
    state = database.Column(database.Integer)
