public class Card {
    private String rank, suit;

    Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }
    //해당 카드의 문양 리턴
    public String getSuit(){
        return suit;
    }
    //해당 카드의 숫자 리턴
    public String getRank(){
        return rank;
    }
    public String toString(){
        return String.format("[%s:%s]", suit, rank);
    }
}
