from models.connection_factory import database

class CloudSource(database.Model):
    __tablename__ = "Files"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    owner = database.Column(database.String)
    index = database.Column(database.String)
    state_content = database.Column(database.String)

    update_date = database.Column(database.DateTime)
