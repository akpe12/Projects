class Player:
    def __init__(self, name = None):
        self.name = name
        self.life = 3
        self.weapon = "None"

    def getLife(self):
        return self.life
    
    def getName(self):
        return self.name
    
    def getWeapon(self):
        return self.weapon
    
    def sentenced(self):
        self.life -= 1
        
    def setWeapon(self, weapon):
        self.weapon = weapon

    def resetInfo(self):
        self.life = 3