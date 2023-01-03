import java.awt.Image;
import javax.swing.ImageIcon;

    //이동
    //방향전환
    //방향에 따른 이미지 변환
    //위치 get

public class Assam {
    private ImageIcon assamImage;
    private int row;
    private int column;
    private String direction;

    public Assam(){
        this.row = 3;
        this.column = 3;
        setDirection("south");
    }

    public ImageIcon getAssamImage(){
        return assamImage;
    }

    public void setDirection(String direction){
        this.direction = direction;
        if (direction.equals("east")){
            assamImage = new ImageIcon("/");
        }
        else if (direction.equals("west")){
            assamImage = new ImageIcon("/");
        }
        else if (direction.equals("south")){
            assamImage = new ImageIcon("/");
        }
        else if (direction.equals("north")){
            assamImage = new ImageIcon("/");
        }
    }

    public String getDirection(){
        return this.direction;
    }
    
    public void setLocation(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow(){
        return this.row;
    }

    public int getColumn(){
        return this.column;
    }

    public void move(){
        int X = this.row;
        int Y = this.column;

        // 북쪽으로
        if (getDirection().equals("north")){
            // 방향 전환 구역으로 갈 때
            if (Y == 0){
                if (X == 0){
                    setDirection("east");
                }
                else if(X == 1 || X == 3 || X == 5){
                    setDirection("south");
                    setLocation(X + 1, Y);
                }
                else if(X == 2 || X == 4 || X == 6){
                    setDirection("south");
                    setLocation(X - 1, Y);
                }
            }
            else {
                setLocation(X, Y - 1);
            }
        }
        // 남쪽으로
        else if (getDirection().equals("south")){
            if (Y == 6){
                if (X == 6){
                    setDirection("west");
                }
                else if (X == 0 || X == 2 || X == 4){
                    setDirection("north");
                    setLocation(X + 1, Y);
                }
                else if (X == 1 || X == 3 || X == 5){
                    setDirection("north");
                    setLocation(X - 1, Y);
                }
            }
            else {
                setLocation(X, Y + 1);
            }
        }
        // 서쪽으로
        else if (getDirection().equals("west")){
            if (X == 0){
                if (Y == 0){
                    setDirection("south");
                }
                else if (Y == 1 || Y == 3 || Y == 5){
                    setDirection("east");
                    setLocation(X, Y + 1);
                }
                else if (Y == 2 || Y == 4 || Y == 6){
                    setDirection("east");
                    setLocation(X, Y - 1);
                }
            }
            else {
                setLocation(X - 1, Y);
            }
        }
        // 동쪽으로
        else if (getDirection().equals("east")){
            if (X == 6){
                if (Y == 6){
                    setDirection("north");
                }
                else if (Y == 0 || Y == 2 || Y == 4){
                    setDirection("west");
                    setLocation(X, Y + 1);
                }
                else if (Y == 1 || Y == 3 || Y == 5){
                    setDirection("west");
                    setLocation(X, Y - 1);
                }
            }
            else {
                setLocation(X + 1, Y);
            }
        }
    }
    
}
