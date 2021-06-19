class global_values(object):

    def __new__(cls):
        if not hasattr(cls, 'instance'):
            cls.instance = super().__new__(cls)
        return cls.instance

    values = {}

    def get(self, name):
        return self.values.get(name)

    def set(self, name: str, value):
        self.values[name] = value

    def get_all(self):
        return self.values

    def set_all(self, values: dict):
        self.values = values

    def set_more(self, values: dict):
        self.values = dict(self.values.items() + values.items())

    def clean(self):
        self.values.clear()