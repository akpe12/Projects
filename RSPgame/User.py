from Player import Player

class User(Player):
    def __init__(self, name = None):
        super().__init__(name)
    def setName(self, name):
        self.name = name
