from flask_sqlalchemy import SQLAlchemy

database: SQLAlchemy = SQLAlchemy(session_options={ "autocommit": True })