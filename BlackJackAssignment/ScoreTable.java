import java.util.HashMap;

public class ScoreTable {
    private HashMap<String, int[]> table = new HashMap<>();

    ScoreTable(){
        // (F)
        // key = rank, value = 점수(int) 쌍의 테이블 생성.
        String[] rank = {"A", "2", "3", "4", "5", "6", "7", "8", 
                        "9", "10", "J", "Q", "K"};
        int[][] value = {{1, 11}, {2}, {3}, {4}, {5}, {6}, 
                        {7}, {8}, {9}, {10}, {10}, {10}, {10}};

        for(int i = 0; i < rank.length; i++){
            table.put(rank[i], value[i]);
        }
    }

    public int[] score(Card card){
        // (G)
        // card의 점수를 리턴한다.
        // ACE의 경우 1 or 11이며, 1 or 11로 구성된 length가 2인 정수배열이 리턴된다.
        int[] myScore = table.get(card.getRank());
        return myScore;
        }
}
