from models.connection_factory import database

class CloudSource(database.Model):
    __tablename__ = "Files"
    id = database.Column(database.Integer, primary_key=True, autoincrement=True)
    owner = database.Column(database.String)
    content = database.Column(database.String)
    address = database.Column(database.String)

    create_date = database.Column(database.DateTime)
