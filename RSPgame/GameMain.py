from User import User
from Cpu import Cpu
from Judge import Judge

class GameMain:
    def __init__(self):
        self.running = True
        self.prepared = False
        
    def start(self):
        self.running = True
    
    def over(self):
        self.running = False
        
    def overMsg(self, player1, player2):
        if player1.getLife() == 0:
            if player1.getName() != None:
                print("목숨 {} {}개 vs {} {}개로 패배했습니다.".format(player1.getName(), 
                            player1.getLife(), player2.getName(), player2.getLife()))
            else:
                print("목숨 user {}개 vs cpu {}개로 패배했습니다.".format(player1.getLife(), player2.getLife()))
        else:
            if player1.getName() != None:
                print("목숨 {} {}개 vs {} {}개로 승리했습니다.".format(player1.getName(), 
                            player1.getLife(), player2.getName(), player2.getLife()))
            else:
                print("목숨 user {}개 vs cpu {}개로 승리했습니다.".format(player1.getLife(), player2.getLife()))
                
        print()
        print("다시 하시겠습니까?")
        print()
        print("네 또는 아니오: ", end="")
        restart = input()
        if restart != "아니오":
            print()
        
        if restart != "네" and restart != "아니오":
            while(restart != "네" and restart != "아니오"):
                print()
                print("다시 묻겠습니다.")
                print("네 또는 아니오: ", end="")
                restart = input()
        
        if restart == "네":
            self.resetGame(player1, player2)
            
        elif restart == "아니오":
            self.over()
            
    def getRunning(self):
        return self.running
        
    def GameStart(self, player1, player2, round):
        if self.prepared == False:
            print("게임을 시작합니다.")
            self.start()
            print()
            
            self.nameMaking(player1)
            self.prepared = True
        
        print("---------- 제 {}라운드 ----------".format(round + 1))
        print("어떤 것을 내시겠습니까?")
        print("가위 / 바위 / 보: ", end="")
        weapon = input()
        
        if weapon not in ["가위", "바위", "보"]:
            while(weapon not in ["가위", "바위", "보"]):
                print()
                print("다시 내주세요.")
                print()
                print("어떤 것을 내시겠습니까?")
                print("가위 / 바위 / 보: ", end="")
                weapon = input()
                
        player1.setWeapon(weapon)
        player2.setWeapon()
        #반복문을 돌면서 가위바위보
        #할 때마다 정할 것 -> 낼 무기
        #무기 물어볼 때, 오타면 다시 묻기
        # while(self.getRunning()):
        #     print("어떤 것을 내시겠습니까?")
        #     print("가위 / 바위 / 보: ", end="")
        #     weapon = input()
        #     if weapon not in ["가위", "바위", "보"]:
        #         while(weapon not in ["가위", "바위", "보"]):
        #             print()
        #             print("다시 내주세요.")
        #             print()
        #             print("어떤 것을 내시겠습니까?")
        #             print("가위 / 바위 / 보: ", end="")
        #             weapon = input()
                    
        #     player1.setWeapon(weapon)
        #     player2.setWeapon()
            
        
    def nameMaking(self, player1):
        print("User의 이름을 정하시겠습니까?")
        print("네 또는 아니오: ", end="")
        nameChoice = input()
        if nameChoice != "네" and nameChoice != "아니오":
            while(nameChoice != "네" and nameChoice != "아니오"):
                print()
                print("다시 대답해주세요.")
                print("네 또는 아니오: ", end="")
                nameChoice = input()
        if nameChoice == "네":
            print()
            print("이름을 정해주세요: ", end="")
            myName = input()
            player1.setName(myName)
            print("나의 이름: " + player1.getName())
            print()
        elif nameChoice == "아니오":
            print()
            print("이름을 user로 설정하겠습니다.")
            player1.setName(None)
            print()
            
    def resetGame(self, player1, player2):
        self.start()
        self.prepared = False
        player1.resetInfo()
        player2.resetInfo()
        # self.GameStart(player1, player2)
            
game = GameMain()
me = User()
op = Cpu()
judge = Judge()
round = 0

while(game.getRunning()):
    game.GameStart(me, op, round)
    judge.whosWin(me, op)
    round += 1
    if me.getLife() == 0 or op.getLife() == 0:
        game.overMsg(me, op)
        if(game.getRunning):
            round = 0