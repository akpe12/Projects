import java.util.Scanner;
import java.util.Vector;

public class BlackJack{
    static final Scanner scan = new Scanner(System.in);
    //user의 총 합산 점수를 mySum에 저장한다.
    static int mySum = 0;
    //dealer의 총 합산 점수를 dealerSum에 저장한다.
    static int dealerSum = 0;

    public static int computeScoreUser(ScoreTable table, Vector<Card> cards){
        //table을 이용해 나의 card들의 점수 총합을 리턴
        //input을 입력받아 ACE의 점수 결정하는 것 또한 수행한다.

        //user의 패에 들어오는 카드의 점수를 myScore에 저장한다.
        int[] myScore;

        //초기 드로우된 2장 점수 합산
        if(cards.size() == 2){
            for(int i = 0; i < 2; i++){
                myScore = table.score(cards.get(i));
                //초기 드로우된 2장 중 ACE카드가 있는지 체크
                if(cards.get(i).getRank().equals("A")){
                    System.out.print(cards.get(i) + " 의 점수를 선택해주세요. " + 
                    "(1 / 11) >> ");
                    //선택한 ACE의 점수를 myAce에 저장한다.
                    int myAce = scan.nextInt();
                    //선택한 ACE의 점수를 mySum에 더해준다.
                    mySum += myAce;
                    System.out.println();
                }
                //초기에 드로우된 2장 중 ACE카드가 아닌 카드는 mySum에 점수를 더해준다.
                else{
                    mySum += myScore[0];
                }
            }
        }
        //user의 패가 3장 이상이라면
        else{
            //이미 점수를 mySum에 더했던 카드들은 계산하지 않도록
            //시작점을 cards.size() - 1로 한다.
            for(int j = cards.size() - 1; j < cards.size(); j++){
                myScore = table.score(cards.get(j));

                if(cards.get(j).getRank().equals("A")){
                    System.out.print(cards.get(j) + " 의 점수를 선택해주세요. " + 
                    "(1 / 11) >> ");
                    int myAce = scan.nextInt();
                    mySum += myAce;
                }
                else{
                    mySum += myScore[0];
                }
            }
        }
        
        //그렇게 합산된 점수를 리턴한다.
        return mySum;
    }

    public static int computeScoreDealer(ScoreTable table, Vector<Card> cards){
        //table을 이용해 컴퓨터의 card들의 점수 총합을 리턴
        //컴퓨터의 ACE계산 방식도 여기서 수행
        //들어온 카드 2개 중 1개만 A -> 11
        //들어온 카드 2개 모두 A -> 11 and 1
        //3번째 카드부터 A가 나왔을 때 -> 1

        //dealer의 패에 들어오는 카드의 점수를 dealerScore에 저장한다.
        int[] dealerScore;

        //초기에 드로우된 2장의 카드의 점수를 계산할 때
        if(cards.size() == 2){
            for(int i = 0; i < 2; i++){
                dealerScore = table.score(cards.get(i));
                //초기에 드로우된 2장의 카드 중 ACE카드가 있다면
                if(cards.get(i).getRank().equals("A")){
                    //지금까지의 dealer 점수의 합이 10점 이하라면
                    //dearlerSum에 11점을 더해준다.
                    //ACE카드가 1장이고 다른 한 장은 ACE카드가 아닌 상황에서
                    //ACE카드의 점수가 더해지기 전에 나올 수 있는
                    //dealerSum의 최대는 10이다.
                    //따라서, 10점이하라면 ACE카드가 한 번도 더해지지 않았다는 말이므로
                    //아래의 상황에 11점을 더해준다.
                    if(dealerSum < 11){
                        dealerSum += 11;
                    }
                    //dealerSum이 11점 이상이라면
                    //ACE카드의 점수가 한 번 더해졌다는 의미이므로
                    //1점만 더해준다.
                    else{
                        dealerSum += 1;
                    }
                }
                //초기에 드로우된 2장 중 ACE카드가 아닌 카드가 있다면
                else{
                    //dealerSum에 그 카드의 점수를 더해준다.
                    dealerSum += dealerScore[0];
                }
            }
        }
        //dealer의 패가 3장 이상이라면
        else{
            //이미 게산했던 카드의 점수는 다시 계산하지 않도록
            //시작점을 cards.size() - 1로 한다.
            for(int j = cards.size() - 1; j < cards.size(); j++){
                dealerScore = table.score(cards.get(j));
                //3번째 장부터 ACE카드가 나오면 1점으로 계산한다.
                if(cards.get(j).getRank().equals("A")){
                    dealerSum += 1;
                }
                else{
                    dealerSum += dealerScore[0];
                }
            }
        }
        //그렇게 합산된 점수를 리턴한다.
        return dealerSum;
    }

    public static boolean is_bust(int score){
        //점수를 받아 21점이 초과하는지 검사
        //초과하면 true 리턴 -> 패배
        if(score > 21){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean checkBlackjack(Vector<Card> cards){
        //카드들을 받아 블랙잭인지 아닌지 리턴
        //초반 2장 드로우 됐을 때만 호출할 예정
        //BlackJack => A + J or Q or K

        //두 개의 elem가 true로 변하면
        //블랙잭의 조건을 충족시킨 것으로 표현한다.
        boolean elem1 = false;
        boolean elem2 = false;
        for(Card my : cards){
            //조건1: 초기에 드로우된 2장 중 1장이 ACE카드인가?
            if(my.getRank().equals("A")){
                elem1 = true;
            }
            //조건2: 초기에 드로우된 2장 중 1장이 J or Q or K인가?
            if(my.getRank().equals("J") || my.getRank().equals("Q") ||
            my.getRank().equals("K")){
                elem2 = true;
            }
        }
        //두 조건을 모두 충족시켰는지 확인한다.
        if((elem1 == true) && (elem2 == true)){
            return true;
        }
        else{
            return false;
        }
    }

    public static void sleep(int time){
        //time만큼 pause후 재개 (단위 => 밀리sec)
        try{
            Thread.sleep(time);
        }
        catch(Exception e){
            System.out.println("InterruptedException 오류 발생!");
        }
    }

    //게임시작 문구 저장
    public static int GameStart(){
        System.out.print("사용할 덱의 개수를 입력해주세요 >> ");
        int deckNum = scan.nextInt();
        
        return deckNum;
    }

    //초기에 각각의 플레이어에게 2장의 카드를 드로우 해준다.
    public static void drawDeck(Player me, Player dealer, CardPool deck){
        //2장의 카드를 드로우 받았을 때
        //공개할 dealer의 카드 2장 중 1장을 무작위로 선정한다.
        int idx = (int)(Math.random() * 2);

        //user와 dealer에게 각각 2장씩 드로우 한다.
        for(int i = 0; i < 2; i++){
            me.addCard(deck.drawCard());
        }
        for(int i = 0; i < 2; i++){
            dealer.addCard(deck.drawCard());
        }

        System.out.println();
        
        System.out.println("당신의 카드는 " + me.getHand() + " 입니다.");
        System.out.println("딜러가 공개한 카드는 " + dealer.get(idx) + " 입니다.");
        System.out.println();
    }

    //초기의 2장 드로우 이후 user의 차례가 진행되는 모든 과정 구현
    public static boolean myTurn(Player me, ScoreTable myTable, CardPool deck){
        //블랙잭 혹은 버스트로 의지와 달리 게임이 종료되는 상황을 gameover에서 체크한다.
        boolean gameover = false;
        //hit or stand라는 선택지를 choice에 담도록 변수를 선언하여 초기화한다.
        String choice = "";

        //myTurn
        //블랙잭의 조건을 충족시킨 상황
        if(checkBlackjack(me.getHand())){
            System.out.println("당신의 BlackJack으로 승리했습니다.");
            //gameover에 true의 값을 준다.
            gameover = true;
            //게임이 종료되었음을 리턴한다.
            return gameover;
        }
        //블랙잭의 조건을 충족시키지 못한 상황
        System.out.println("당신의 차례입니다.");
        //user가 stand를 원하지 않는 동안
        while(!choice.equals("stand")){
            //초기에 받은 2장의 카드의 점수를 계산하여 mySum에 저장한다.
            computeScoreUser(myTable, me.getHand());

            //mySum에 있는 user의 총 점수가 21점이 넘어 버스트가 된 상황
            if(is_bust(mySum)){
                System.out.println("패의 총합이 21을 초과하여 패배했습니다.");
                //gameover에 true 값을 준다.
                gameover= true;
                //게임이 종료되었음을 리턴한다.
                break;
            }

            System.out.print("카드를 더 받으시겠습니까? (hit/stand) >> ");
            choice = scan.next();
            System.out.println();

            //user가 hit or stand가 아닌 오타를 적었을 경우
            //반복해서 선택지를 묻는다.
            if((!choice.equals("hit")) && (!choice.equals("stand"))){
                while((!choice.equals("hit")) && (!choice.equals("stand"))){
                    System.out.print("카드를 더 받으시겠습니까? (hit/stand) >> ");
                    choice = scan.next();
                    System.out.println();
                }
            }

            //user가 hit를 원한다면
            if(choice.equals("hit")){
                //user의 패에 카드를 1장 추가한다.
                me.addCard(deck.drawCard());
                
                System.out.println(me.get(me.getLastIdx()) + " 를 받았습니다.");
                System.out.println("당신의 카드는 " + me.getHand() + " 입니다.");
                System.out.println();

            }
        }
        //user의 의지인 stand로 차례를 종료시킨 경우로써 false를 리턴한다.
        return gameover;
    }

    //user의 차례 이후 이어지는 dealer의 차례 구현
    public static boolean dealerTurn(Player dealer, ScoreTable myTable, CardPool deck){
        boolean gameover = false;
        
        System.out.println("당신의 차례가 끝났습니다.");
        System.out.println();

        System.out.println("딜러의 차례입니다.");
        System.out.println("딜러의 카드는 " + dealer.getHand() + " 입니다.");
        System.out.println();
        
        //dealer가 초반에 드로우 받은 2장의 카드가 블랙잭의 조건을 충족시킨다면
        if(checkBlackjack(dealer.getHand())){
            System.out.println("딜러의 BlackJack으로 패배했습니다.");   
            gameover = true;            
        }
        //dealer가 초반에 드로우 받은 2장의 카드가 블랙잭의 조건을 충족시키지 못한다면
        else{
            while(true){
                //지금까지 dealer가 받은 모든 카드의 합을
                //dealerScore에 저장한다.
                //dealerScore와 dealerSum을 따로 만든 이유는
                //dealerScore에서 카드의 점수를 계산해놓고
                //정리한 값을 dealerSum에 저장하기 위해서이다.
                //computeScoreUser 내의 myScore와 같은 기능을 한다.
                int dealerScore = computeScoreDealer(myTable, dealer.getHand());
                
                //dealer의 점수가 21점이 넘었다면
                if(is_bust(dealerScore)){
                    System.out.println("딜러 패의 총합이 21을 초과하여 승리했습니다.");
                    gameover = true;
                    break;
                }
                //dealerScore에 저장된 dealer의 점수가 16점 이하라면
                if(dealerScore <= 16){
                    //카드 한 장을 hit한다.
                    dealer.addCard(deck.drawCard());
                    System.out.println(dealer.get(dealer.getLastIdx()) + " 를 받았습니다.");
                    System.out.println("딜러의 카드는 " + dealer.getHand() + " 입니다.");
                    System.out.println();
                    //3초 이후 진행시킨다.
                    sleep(3000);
                }
                //dealerScore에 저장된 dealer의 점수가 17점 이상이라면
                else if(dealerScore >= 17){
                    //차례를 종료시킨다.
                    System.out.println("딜러의 차례가 끝났습니다.");
                    System.out.println();
                    break;
                }
            }
        }
        //dealer의 점수가 17점 이상 21점 이하인 상황으로
        //gameover가 false를 리턴한다.
        return gameover;
    }

    //게임의 결과를 심판한다.
    public static void GameResult(){
        System.out.println("유저: " + mySum + " vs 딜러: " + dealerSum);

        if(mySum > dealerSum){
            System.out.println("승리했습니다.");
        }
        else if(mySum < dealerSum){
            System.out.println("패배했습니다.");
        }
        else{
            System.out.println("무승부");
        }
    }

    public static void main(String[] args){
        //main 실행부1
        //게임에서 사용할 덱의 개수를 설정한다.
        int deckNum = GameStart();
        //게임에서 사용할 덱을 생성한다.
        CardPool deck = new CardPool(deckNum);
        //플레이어 user를 생성한다.
        Player me = new Player();
        //플레이어 dealer를 생성한다.
        Player dealer = new Player();
        //점수를 계산할 테이블을 생성한다.
        ScoreTable myTable = new ScoreTable();
        //user와 dealer의 턴이 블랙잭 혹은 버스트에 의해 강제 종료 되었는지
        //혹은 자의로 종료되었는지의 결과를 각각 myResult 그리고 dealerResult에 담는다.
        boolean myResult;
        boolean dealerResult;

        //초기 2장의 카드를 각각에게 드로우 해준다.
        drawDeck(me, dealer, deck);
        //user의 턴이 자의로 끝나는지 아닌지의 결과를 myResult에 저장한다.
        myResult = myTurn(me, myTable, deck);
        //user의 턴이 자의로 끝났다면
        if(myResult == false){
            //dealer의 턴을 시작한다.
            dealerResult = dealerTurn(dealer, myTable, deck);
            //dealer의 턴이 자의로 끝났다면
            if(dealerResult == false){
                //게임을 종료하고
                //결과를 출력한다.
                GameResult();
            }
        }
    }
}