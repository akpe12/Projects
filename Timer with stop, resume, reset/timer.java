import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

//여기서 굳이 JLabel 상속해주지 않아도 JLabel 객체 만들기가 가능하다.
//상속을 하는 이유는 객체를 만들지 않고도 해당 클래스의 멤버 메소드, 변수들을 이용할 수 있게 되기 때문에 상속을 하는 것이므로
//객체를 만들 거라면 상속을 해주지 않아도 된다.
//상속을 하여 객체를 만들지 않고도 클래스 필드 사용할 수 있게 하기 or 상속을 하지 않고 객체를 만들어 해당 클래스의 클래스 필드 사용하기
class myTimer extends JLabel implements Runnable{
    private JLabel timer;
    volatile boolean isSuspended = false;
    private int time = 0;

    public myTimer(JLabel timer){
        this.timer = timer;
    }

    public void run(){
        while(true){
            if(!isSuspended){
                try{
                    timer.setText(Integer.toString(time));
                    time++;
                    Thread.sleep(1000);
                }
                catch(InterruptedException e){
                    return;
                }
            }
        }
    }
    public void suspend(){
        isSuspended = true;
    }
    public void resume(){
        isSuspended = false;
    }
    // 다시 시작용
    public int getTime(){
        return time;
    }
    public void resetTime(){
        time = 0;
    }
}
public class Main extends JFrame{
    private int entered = 0;
    public Main(){
        setTitle("Timer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        JLabel newTimer = new JLabel();
        newTimer.setFont(new Font("Arial", Font.ITALIC, 80));
        c.add(newTimer);
        myTimer exTimer = new myTimer(newTimer);

        c.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(entered == 0){
                    exTimer.suspend();
                    entered++;
                }
                else if(entered == 1){
                    exTimer.resume();
                    entered++;
                }
                else{
                    exTimer.resetTime();
                    entered = 0;
                }
            }
        });

        setSize(300, 300);
        setVisible(true);
        c.setFocusable(true);
        c.requestFocus();

        Thread myTh = new Thread(exTimer);
        myTh.start();
    }
    public static void main(String[] args){
        new Main();
    }
}
