import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.Runnable;
import java.util.*;

//메인부 안에 jframe 설정, mouselistener설정, thread 설정까지
public class Main extends JFrame implements Runnable{
    private Thread th;
    public Main(){
        setTitle("Vibrate");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);
        setFocusable(true);
        requestFocus();

        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if(!th.isAlive()){
                    return;
                }
                else{
                    th.interrupt();
                }
            }
        });
        th = new Thread(this);
        th.start();
    }
    public void run(){
        Random r = new Random();
        //마우스로 누르기 전 계속 위치를 옮겨줘야 진동 효과가 나므로
        //while(true)를 잊지 말자
        while(true){
            try{
                //몇 초마다 프레임을 움직이게 만들 것인지
                //시간 설정
                Thread.sleep(20);
            }
            catch(InterruptedException e){return;}
            //위치 결정의 핵심부
            int x = getX() + r.nextInt() % 5;
            int y = getY() + r.nextInt() % 5;
            setLocation(x, y);
        }
    }
    public static void main(String[] args){
        new Main();
    }
}
