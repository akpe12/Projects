import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

public class Main extends JFrame{
    public Main(){
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new myPanel());
        setSize(300, 300);
        setVisible(true);
    }
    public static void main(String[] args){
        new Main();
    }
    class myPanel extends JPanel{
        private Vector<Point> vStart = new Vector<Point>();
        private Vector<Point> vEnd = new Vector<Point>();

        public myPanel(){
            MyMouseListener lis = new MyMouseListener();
            addMouseListener(lis);
            addMouseMotionListener(lis);
        }
        class MyMouseListener extends MouseAdapter{
            public void mousePressed(MouseEvent e){
                Point startP = e.getPoint();
                vStart.add(startP);
            }
            public void mouseReleased(MouseEvent e){
                Point endP = e.getPoint();
                vEnd.add(endP);
                repaint();
            }
        }
        //핵심!
        //point에 저장된 좌표를 이용하여 for문 안에서 위치를 집어넣어준다.
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            g.setColor(Color.BLUE);
            for(int i = 0; i < vStart.size(); i++){
                Point s = vStart.elementAt(i);
                Point e = vEnd.elementAt(i);
                int x = Math.min(s.x, e.x);
                int y = Math.min(s.y, e.y);
                int width = Math.abs(s.x - e.x);
                int height = Math.abs(s.y - e.y);

                g.drawOval(x, y, width, height);
            }
        }
    }
}
