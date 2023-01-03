import javax.swing.JLabel;
import java.awt.*;
import javax.swing.*;

public class Checker extends JLabel{
    private ImageIcon south = new ImageIcon("/");
    private ImageIcon north = new ImageIcon("/");
    private ImageIcon east = new ImageIcon("/");
    private ImageIcon west = new ImageIcon("/");
    private String direction;
    private int row;
    private int col;

    public void setRow(int row){
        this.row = row;
    }
    public void setColumn(int col){
        this.col = col;
    }
    public void setDirection(String direction){
        this.direction = direction;
    }
    public int getRow(){
        return row;
    }
    public int getColumn(){
        return col;
    }
    public String getDirection(){
        return direction;
    }
    public ImageIcon getChecker(String direction){
        if(direction.equals("south")){
            return southChecker();
        }
        else if(direction.equals("north")){
            return northChecker();
        }
        else if(direction.equals("east")){
            return eastChecker();
        }
        else{
            return westChecker();
        }
    }
    public ImageIcon southChecker(){
        Image genie = south.getImage();
        genie = genie.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        ImageIcon Newgenie = new ImageIcon(genie);        

        return Newgenie;
    }
    public ImageIcon northChecker(){
        Image genie = north.getImage();
        genie = genie.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        ImageIcon Newgenie = new ImageIcon(genie);        

        return Newgenie;
    }
    public ImageIcon eastChecker(){
        Image genie = east.getImage();
        genie = genie.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        ImageIcon Newgenie = new ImageIcon(genie);        

        return Newgenie;
    }
    public ImageIcon westChecker(){
        Image genie = west.getImage();
        genie = genie.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        ImageIcon Newgenie = new ImageIcon(genie);        

        return Newgenie;
    }
}
