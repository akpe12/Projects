import java.util.List;
import java.util.ArrayList;

public class Player {
    private int totalDirham;
    private int dirham1;
    private int dirham5;
    private List<Integer> color = new ArrayList<>();
    private int carpet;
    private int resultPoint;
    private boolean isAlive;

    public Player(int num, boolean isAlive){
        this.totalDirham = 30;
        this.dirham1 = 5;
        this.dirham5 = 5;
        this.carpet = setDefaultCarpet(num);
        this.isAlive = isAlive;
        this.resultPoint = 0;
    }

    public int getTotalDirham() {
        this.totalDirham = getDirham1() + getDirham5() * 5;
        return totalDirham;
    }

    public void setDirham1(int dirham1) {
        this.dirham1 = dirham1;
    }

    public int getDirham1() {
        return dirham1;
    }

    public void addDirham1(){
        dirham1++;
    }

    public void minusDirham1(){
        dirham1--;
    }

    public void setDirham5(int dirham5) {
        this.dirham5 = dirham5;
    }

    public int getDirham5() {
        return dirham5;
    }

    public void addDirham5(){
        dirham5++;
    }

    public void minusDirham5(){
        dirham5--;
    }
    
    public void resetDirmham(){
        this.dirham1 = 0;
        this.dirham5 = 0;
    }

    public int getCarpet() {
        return carpet;
    }

    public void canceledCarpet(){
        this.carpet++;
    }

    public void setCarpet() {
        this.carpet--;
    }

    public int setDefaultCarpet(int num){
        if(num == 2){
            carpet = 24;            
        }
        else if(num == 3){
            carpet = 15;
            // carpet = 3;
        }
        else if(num == 4){
            carpet = 12;
            // carpet = 2;
        }

        return carpet;
    }        

    public void setColor(int color) {
        this.color.add(color);
    }

    public List<Integer> getColor() {
        return color;
    }

    public void setDead(){
        this.isAlive = false;
    }

    public boolean getIsAlive(){
        return isAlive;
    }

    public void setResultPoint(int resultPoint){
        this.resultPoint = resultPoint + getTotalDirham();
    }

    public int getResultPoint(){
        return resultPoint;
    }
}
