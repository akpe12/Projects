from Player import Player
import random

class Cpu(Player):
    def __init__(self, name = "CPU"):
        super().__init__(name)
        self.weaponList = ["가위", "바위", "보"]
    
    #랜덤으로 가위바위보중 하나 뽑아서 weapon으로 설정한다.
    def setWeapon(self):
        num = random.randint(0, 2)
        super().setWeapon(self.weaponList[num])
        