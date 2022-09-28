import random
import time

class Judge:
    def __init__(self):
        self.coin = ["앞면", "뒷면"]
        self.result = ""
    
    #가위바위보 결과 처리
    def whosWin(self, player1, player2):
        if player1.getWeapon() == "가위":
            if player2.getWeapon() == "가위":
                self.result = "draw"
            elif player2.getWeapon() == "보":
                self.result = "win"
            elif player2.getWeapon() == "바위":
                self.result = "lose"
                
        elif player1.getWeapon() == "바위":
            if player2.getWeapon() == "바위":
                self.result = "draw"
            elif player2.getWeapon() == "가위":
                self.result = "win"
            elif player2.getWeapon() == "보":
                self.result = "lose"
                
        elif player1.getWeapon() == "보":
            if player2.getWeapon() == "보":
                self.result = "draw"
            elif player2.getWeapon() == "바위":
                self.result = "win"
            elif player2.getWeapon() == "가위":
                self.result = "lose"
                
        self.getResult(player1, player2)
    
    #승패 판단
    def getResult(self, player1, player2):
        print()
        print("가위...")
        time.sleep(1.5)
        print()
        print("바위...")
        time.sleep(1.5)
        print()
        print("보!")
        
        if player1.getName() != None:
            print()
            print("{} : {} vs {} : {}"
                  .format(player1.getName(), player1.getWeapon(),
                        player2.getName(), player2.getWeapon()))
            print()
        else:
            print()
            print("user : {} vs cpu : {}"
                  .format(player1.getWeapon(), player2.getWeapon()))
            print()
        
        if self.result == "win":
            if player1.getName() != None:
                print(player1.getName() + " 이(가) 승리했습니다!")
            else:
                print("승리했습니다!")
                
            
        elif self.result == "draw":
            print("비겼습니다.")
            #코인 던지기
            self.coinToss(player1, player2)
            
        elif self.result == "lose":
            if player1.getName() != None:
                print(player1.getName() + " 이(가) 졌습니다.")
            else:
                print("졌습니다.")
                
        self.Judgement(player1, player2)    
    
    #가위바위보 결과에 따른 업보
    def Judgement(self, player1, player2):
        if self.result == "win":
            self.sentence(player2)
        elif self.result == "draw":
            pass
            #동전 던지기
        elif self.result == "lose":
            self.sentence(player1)
        
        self.currentState(player1, player2)
        
    #몫 빼앗기         
    def sentence(self, loser):
        loser.sentenced()
        print()
        if loser.getName() != None:
            print(loser.getName() + " 이(가) 목숨 1개를 잃었습니다.")
        else:
            print("User가 목숨 1개를 잃었습니다.")
        print()
        
    #현재 스코어 알려주기
    def currentState(self, player1, player2):
        if player1.getLife() != 0 and player2.getLife() != 0:
            if player1.getName() != None:
                    print("현재 목숨 {} {}개 vs {} {}개입니다."
                        .format(player1.getName(), player1.getLife(),
                                player2.getName(), player2.getLife()))
            else:
                print("현재 목숨 user {}개 vs cpu {}개입니다."
                        .format(player1.getLife(),player2.getLife()))
    #코인 토스        
    def coinToss(self, player1, player2):
        print()
        print("동전 던지기로 목숨을 잃을 사람을 정합니다.")
        print()
        
        print("동전의 앞면/뒷면 중 어떤 쪽을 선택하시겠습니까?")
        print("앞면 / 뒷면 : ", end="")
        myCoin = input()
        print()
        if myCoin not in self.coin:
            while myCoin not in self.coin:
                print("다시 선택해주세요.")
                print("앞면 / 뒷면 : ", end="")
                myCoin = input()
                print()
        cpuCoin = ""
        
        for part in range(len(self.coin)):
            if self.coin[part] != myCoin:
                cpuCoin = self.coin[part]
        part = random.randint(0, 1)
        resultCoin = self.coin[part]
        
        print("휘리릭...")
        time.sleep(1.5)
        print()
        print("탁!")
        time.sleep(1)
        print()
        
        print("동전은 {}이 나왔습니다.".format(resultCoin))
        
        if myCoin == resultCoin:
            self.sentence(player2)
        elif cpuCoin == resultCoin:
            self.sentence(player1)
        
