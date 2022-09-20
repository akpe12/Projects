import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


//Label로 바 만들기 with fill, consume기능
class IBar extends JLabel{
    private int stateOfBar = 0;
    private int MAX_BAR;

    public IBar(int MAX_BAR){
        this.MAX_BAR = MAX_BAR;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.RED);
        int width = (int)(((double)(this.getWidth()))/MAX_BAR*stateOfBar);
        if(width == 0){
            return;
        }
        else{
            g.fillRect(0, 0, width, this.getHeight());
        }
    }
    public synchronized void fill(){
        stateOfBar++;
        repaint();
        notify();
    }
    public synchronized void consume(){
        if(stateOfBar == 0){
            try{
                wait();
            }
            catch(InterruptedException e){
                return;
            }
        }
        stateOfBar--;
        repaint();
        notify();
    }
}
//consumer thread 만들기
class ConThread extends Thread{
    private IBar bar;

    public ConThread(IBar bar){
        this.bar = bar;
    }

    public void run(){
        while(true){
            try{
                sleep(2000);
                bar.consume();
            }
            catch(InterruptedException e){
                return;
            }
        }
    }
}
//메인부 프레임 with 키 누르면 막대 채워지도록 기능 구현
public class Main extends JFrame{
    private IBar myBar = new IBar(100);
    public Main(){
        setTitle("Filling the bar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(null);
        myBar.setBackground(Color.GRAY);
        myBar.setSize(300, 20);
        myBar.setLocation(20, 50);
        myBar.setOpaque(true);
        c.add(myBar);

        c.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                myBar.fill();
            }
        });

        setSize(350, 300);
        setVisible(true);
        c.setFocusable(true);
        c.requestFocus();

        ConThread myTh = new ConThread(myBar);
        myTh.start();
    }
    //실행부
    public static void main(String[] args){
        new Main();
    }
}
