import java.util.Collections;
import java.util.Stack;

public class CardPool {
    private Stack<Card> cards = new Stack<Card>();

    CardPool(int num_decks){
        //(D)
        //num_decks개의 덱을 생성하고 셔플한 후 추가한다.
        Stack<Card> deck = new Stack<Card>();
        String suit[] = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String rank[] = {"A", "2", "3", "4", "5", "6", "7", "8", "9",
                        "10", "J", "Q", "K"};
        //num_decks개의 덱 생성
        for(int i = 0; i < num_decks; i++){
            for(int j = 0; j < suit.length; j++){
                for(int k = 0; k < rank.length; k++){
                    Card card = new Card(suit[j], rank[k]);
                    deck.add(card);
                }
            }
        }
        //덱 셔플
        Collections.shuffle(deck);
        //카드에 셔플한 덱을 추가
        cards = deck;
        //게임에 사용할 카드의 개수 출력
        System.out.println(num_decks + "개의 덱, " + 
        cards.size() + "개의 카드를 사용합니다."); //덱에 들어있는 카드 개수
    }
    
    public Card drawCard(){
        //(E)
        //카드들로부터 한 장 뽑는다.
        Card drawedCard = cards.pop();
        return drawedCard;
    }
}
