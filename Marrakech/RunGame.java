import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class RunGame extends JFrame{
    private ImageIcon icon = new ImageIcon("/");
    private ImageIcon up = new ImageIcon("/");
    private ImageIcon down = new ImageIcon("/");
    private ImageIcon left = new ImageIcon("/");
    private ImageIcon right = new ImageIcon("/");
    private JLabel Up = new JLabel(up);
    private JLabel Down = new JLabel(down);
    private JLabel Left = new JLabel(left);
    private JLabel Right = new JLabel(right);
    private JLabel carpet;
    private JLabel[] stateLabel = new JLabel[4];
    private JLabel[] carpetZone = new JLabel[12];
    private JMenuBar mb;
    private JMenu game;
    private JMenuItem gameStart;
    private JTextArea logText;
    private JTextArea connectedState;
    private JPanel mainPane;
    private JPanel gridPane;
    private JPanel infoPane;
    private JPanel infoPartPane;
    private JPanel infoPane1;    
    private JPanel infoPane3;
    private JPanel player1;
    private JPanel player2;
    private JPanel player3;
    private JPanel player4;
    private JPanel[][] square = new JPanel[7][7];
    private JPanel[][] carpetSquare = new JPanel[7][7];
    private BoardPane boardPane;
    private Checker checker = new Checker();
    private JLabel marker;
    private JLabel infoPane2;
    private Player[] player;
    private Color removedColor;
    private Color[] color = {Color.RED, Color.ORANGE, Color.BLUE, Color.GREEN};
    private int turn = 0;
    private int carpetCount = 0;
    private int playerNum;
    private int red;
    private int orange;
    private int blue;
    private int green;
    private List<Integer> drawPlayers = new ArrayList<Integer>();
    private List<Integer> carpetColorNames = new ArrayList<Integer>();
    private String num;
    private String removedCarpetOwner;
    private String firstCarpetOwner;    
    private String[] colorNames = {"??????", "??????", "??????", "??????"};
    private JButton throwingDiceBtn;
    private JButton passPlayerTurnBtn;
    private JButton justStartBtn;
    private JButton gameStartWithCpuBtn;
    private JButton gameStartOnlineBtn;    
    private JButton newGameBtn;
    private JButton joinGameBtn;
    private boolean canceled = false;
    private boolean isMoving = false;
    private boolean isRunning = false;
    private boolean isCpu = false;
    private boolean isCpuPlaying = false;
    private boolean isOnline = false;
    private boolean myTurn = false;
    private boolean isHostTurn = false;
    private boolean isClientTurn = false;
    private boolean isCarpetOnTop = false;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    
    public RunGame(){
        setTitle("Marrakech");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new GridLayout(1, 2));

        createMenuBar();
        createBoard();
        createInfo();

        setSize(800, 500);
        //??? ?????? ?????? ??????
        setResizable(false);
        setVisible(true);
    }
    public void createMenuBar(){
        //???????????? ?????? ?????????
        mb = new JMenuBar();

        game = new JMenu("?????? ??????");
        gameStart = new JMenuItem("?????? ????????????");        

        gameStart.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new gameMode();                
            }
        });        

        game.add(gameStart);        
        mb.add(game);

        setJMenuBar(mb);
    }
    
    public void createBoard(){        

        //?????? ?????? ??????(?????? ????????? ?????? ????????? ?????? ????????? ??? ?????? ???)
        mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());
        mainPane.setBorder(new EmptyBorder(50, 30, 50, 20));

        //?????? ???????????? ??????????????? ??? ??????
        boardPane = new BoardPane();
        boardPane.setLayout(new BorderLayout());
        boardPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        //7X7 ?????? ???????????? ?????? ???????????? ?????? gridPane ??????
        gridPane = new JPanel();
        gridPane.setLayout(new GridLayout(7, 7));
        gridPane.setOpaque(false);        

        for(int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                carpetSquare[i][j] = new JPanel();
                carpetSquare[i][j].setLayout(new BorderLayout());
                carpetSquare[i][j].setOpaque(false);
                carpetSquare[i][j].setName(Integer.toString(i) + Integer.toString(j));
                
                gridPane.add(carpetSquare[i][j]);
            }
        }

        for(int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                square[i][j] = new JPanel();
                square[i][j].setLayout(new BorderLayout());
                square[i][j].setOpaque(false);

                carpetSquare[i][j].add(square[i][j]);
            }
        }

        //7X7 ????????? ????????? ????????? ????????? ?????????        
        boardPane.add(gridPane);

        //7X7 ????????? ????????? ?????? ????????? ????????? mainPane??? ?????????
        mainPane.add(boardPane);

        //?????? ?????? ??????
        marker = new JLabel(checker.southChecker());
        checker.setRow(3);
        checker.setColumn(3);
        square[checker.getRow()][checker.getColumn()].add(marker);
        checker.setDirection("south");
        marker.setVisible(false);


        // ????????? ?????? ?????? ?????? ??????
        for(int i = 0; i < carpetZone.length; i++){
            carpetZone[i] = new JLabel();
        }

        for(int i = 0; i < carpetZone.length; i++){
            carpetZone[i].addMouseListener(new MouseAdapter(){
                @Override
                // ???????????? ?????? ?????? ?????????????????? ???????????? ??????.
                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // super.mouseEntered(e);
                    Component comp = (Component)e.getSource();                    
                    if(playerNum == 2){
                        if(nowPlaying(turn).getCarpet() >= 13){
                            comp.setBackground(color[nowPlaying(turn).getColor().get(0)]);        
                        }
                        else{
                            comp.setBackground(color[nowPlaying(turn).getColor().get(1)]);
                        }
                    }
                    else{
                        comp.setBackground(color[nowPlaying(turn).getColor().get(0)]);
                    }
                }

                @Override
                // ??????????????? ???????????? ???????????? ?????? ?????? ????????? ??????????????? ??????.
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // super.mouseExited(e);
                    Component comp = (Component)e.getSource();
                    if(comp.getParent().getComponentCount() == 3){
                        comp.setBackground(Color.LIGHT_GRAY);
                    }
                    else{
                        comp.setBackground(Color.LIGHT_GRAY);
                    }
                }

                @Override
                // ???????????? ????????? ???????????? ??????????????? ??????.
                public void mousePressed(MouseEvent e) {
                    Component comp = (Component)e.getSource();    
                                    
                    carpetCount++;                    
                    carpet = new JLabel();                    

                    carpet.addMouseListener(new MouseAdapter(){
                        // @Override
                        // ????????? ????????? ??? ????????? ?????? ??????
                        public void mousePressed(MouseEvent e){
                            Component comp = (Component)e.getSource();
                            
                            if(carpetCount == 1 && e.getButton() == MouseEvent.BUTTON3){  
                                // ??? ???????????? ????????? ??? ????????? ??????                              
                                if(removedColor == null && comp.getParent().getName().equals(firstCarpetOwner)){                                    
                                    if(myTurn == true){
                                        try{
                                            writer = new PrintWriter(socket.getOutputStream(), true);                            
                                            writer.println("SETCANCELCARPETING-" + turn + "-" + comp.getParent().getName());
                                        }
                                        catch(IOException er){
                                            System.out.println(er.getMessage());
                                        }
                                    }
                                    comp.getParent().remove(comp);
                                    carpetCount--;
                                    nowPlaying(turn).canceledCarpet();                                    
                                }
                                // ????????? ????????? ???????????? ????????? ??? ????????? ??????
                                else if(removedColor != null && comp.getParent().getName().equals(firstCarpetOwner)){                                    
                                    String tempX = firstCarpetOwner.substring(0, 1);
                                    String tempY = firstCarpetOwner.substring(1, 2);                                    
                                    int rx = Integer.parseInt(tempX);
                                    int ry = Integer.parseInt(tempY);

                                    if(myTurn == true){
                                        try{
                                            writer = new PrintWriter(socket.getOutputStream(), true);                            
                                            writer.println("SETCANCELCARPETINGONTOP-" + turn + "-" + comp.getParent().getName() + "-" + 
                                            tempX + "-" + tempY + "-" + removedCarpetOwner + "-" + removedColor.getRed() + "-" + removedColor.getGreen() + "-" + removedColor.getBlue());
                                        }
                                        catch(IOException er){
                                            System.out.println(er.getMessage());
                                        }
                                    }
                                    
                                    comp.getParent().remove(comp); 
                                    carpet.setName(removedCarpetOwner);
                                    carpet.setBackground(removedColor);

                                    carpetSquare[rx][ry].add(carpet);                                                                       
                                    carpetCount--;
                                    nowPlaying(turn).canceledCarpet();
                                }
                                else{
                                    logText.append("????????? ??? ?????? ??????????????????." + '\n');
                                    logText.setCaretPosition(logText.getDocument().getLength());
                                }
                                canceled = true;
                            }
                            revalidate();
                            repaint();
                        }
                    });

                    // ????????? ?????? ???????????? ?????? ??????                    
                    if(carpetCount == 1){
                        firstCarpetOwner = comp.getParent().getName();
                    }

                    // ????????? ????????? ??????????????? ??? ?????? ?????? ????????? ??? ???????????? ??????.
                    if(carpetCount == 2){                        
                        setCarpetZoneUnvisible();
                    }

                    // ????????? ?????? ??????
                    if(playerNum == 2){
                        if(nowPlaying(turn).getCarpet() >= 13){
                            carpet.setBackground(color[nowPlaying(turn).getColor().get(0)]);        
                        }
                        else{
                            carpet.setBackground(color[nowPlaying(turn).getColor().get(1)]);
                        }
                    }
                    else{
                        carpet.setBackground(color[nowPlaying(turn).getColor().get(0)]);
                    }

                    // ???????????? ????????? ?????? ????????????
                    // ???????????? ????????? ?????? ??? ????????? ??? ??????
                    if(comp.getParent().getComponentCount() == 3){
                        removedColor = comp.getParent().getComponent(2).getBackground();
                        removedCarpetOwner = comp.getParent().getComponent(2).getName();
                        if(isOnline == true && myTurn == true){
                            try{
                                isCarpetOnTop = true;
                                writer = new PrintWriter(socket.getOutputStream(), true);
                                writer.println("SETCARPETINGONTOP-" + turn + "-" + comp.getParent().getName() + "-" 
                                + carpet.getBackground().getRed() + "-" + carpet.getBackground().getGreen() + "-" + carpet.getBackground().getBlue());                                
                            }
                            catch(IOException er){
                                System.out.println(er.getMessage());
                            }
                        }                        
                        comp.getParent().remove(comp.getParent().getComponent(2));                                                
                    }
                    else{
                        removedColor = null;
                    }

                    nowPlaying(turn).setCarpet();
                    carpet.setName(Integer.toString(turn + 1));
                    carpet.setOpaque(true);
                    comp.getParent().add(carpet);                    
                    comp.setVisible(false);
                    
                    // ????????? ?????? ????????? ?????? ??????(???????????? ????????? ?????? ???????????? ????????? ???)
                    if(myTurn == true && isCarpetOnTop == false){
                        try{
                            writer = new PrintWriter(socket.getOutputStream(), true);                            
                            writer.println("SETCARPETING-" + turn + "-" + comp.getParent().getName() + "-" 
                            + carpet.getBackground().getRed() + "-" + carpet.getBackground().getGreen() + "-" + carpet.getBackground().getBlue());
                        }
                        catch(IOException er){
                            System.out.println(er.getMessage());
                        }
                    }
                    isCarpetOnTop = false;
                }
            });
        }

        // ?????? ?????? ???????????? ???????????? ??????
        int x = checker.getRow();
        int y = checker.getColumn();
        square[x - 1][y].add(Up);
        square[x + 1][y].add(Down);
        square[x][y - 1].add(Left);
        square[x][y + 1].add(Right);
        Up.setVisible(false);
        Down.setVisible(false);
        Left.setVisible(false);
        Right.setVisible(false);


        Up.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                checker.setDirection("north");
                marker.setIcon(checker.getChecker(checker.getDirection()));

                setDirectionArrowUnvisible();
                throwingDiceBtn.setEnabled(true);
                logText.append("????????? ???????????????." + '\n');
                
                if(isOnline == true){
                    try{
                        writer = new PrintWriter(socket.getOutputStream(), true);
                        writer.println("SETDIRECTION-north");                    
                    }
                    catch(IOException er){
                        System.out.println(er.getMessage());
                    }
                }                
            }
        });
        Down.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                checker.setDirection("south");
                marker.setIcon(checker.getChecker(checker.getDirection()));

                setDirectionArrowUnvisible();
                throwingDiceBtn.setEnabled(true);
                logText.append("????????? ???????????????." + '\n');

                if(isOnline == true){
                    try{
                        writer = new PrintWriter(socket.getOutputStream(), true);
                        writer.println("SETDIRECTION-south");
                    }
                    catch(IOException er){
                        System.out.println(er.getMessage());
                    }
                }
            }
        });
        Left.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                checker.setDirection("west");
                marker.setIcon(checker.getChecker(checker.getDirection()));

                setDirectionArrowUnvisible();
                throwingDiceBtn.setEnabled(true);
                logText.append("????????? ???????????????." + '\n');

                if(isOnline == true){
                    try{
                        writer = new PrintWriter(socket.getOutputStream(), true);
                        writer.println("SETDIRECTION-west");
                    }
                    catch(IOException er){
                        System.out.println(er.getMessage());
                    }
                }
            }
        });
        Right.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                checker.setDirection("east");
                marker.setIcon(checker.getChecker(checker.getDirection()));

                setDirectionArrowUnvisible();
                throwingDiceBtn.setEnabled(true);
                logText.append("????????? ???????????????." + '\n');

                if(isOnline == true){
                    try{
                        writer = new PrintWriter(socket.getOutputStream(), true);
                        writer.println("SETDIRECTION-east");                    
                    }
                    catch(IOException er){
                        System.out.println(er.getMessage());
                    }
                }            
            }
        });

        add(mainPane);
    }
    
    // ????????? ?????? ?????? ?????? ??????
    public void possibleZone(int x, int y){
        int[] dx = {-2, -1, -1, -1, 0, 0, 0, 0, 1, 1, 1, 2};
        int[] dy = {0, -1, 0, 1, -2, -1, 1, 2, -1, 0, 1, 0};

        for(int i = 0; i < carpetZone.length; i++){
            if(!(x + dx[i] < 0 || x + dx[i] > 6 || y + dy[i] < 0 || y + dy[i] > 6)){
                carpetZone[i].setBackground(Color.LIGHT_GRAY);                
                carpetSquare[x + dx[i]][y + dy[i]].add(carpetZone[i]);
                carpetZone[i].setOpaque(true);
                carpetZone[i].setVisible(true);

                // ????????? ?????? ???????????? ?????? ?????? possible ????????? ?????? ??????
                if(!(carpet == null)){                    
                    carpetSquare[x + dx[i]][y + dy[i]].setComponentZOrder(carpetZone[i], 1);
                }
                
                // ?????? ????????? ???????????? ????????? ??? ????????? ??????
                if(carpetSquare[x + dx[i]][y + dy[i]].getComponentCount() == 3 &&
                    carpetSquare[x + dx[i]][y + dy[i]].getComponent(2).getName().equals(Integer.toString(turn + 1))){
                    carpetZone[i].setVisible(false);
                    carpetSquare[x + dx[i]][y + dy[i]].remove(carpetZone[i]);
                }                                
            }
        }        
    }
    
    // ???????????? ?????? ????????? ??? ???????????? ???????????? ?????? ?????? ??????
    public void secondPossibleZone(int x, int y){
        // carpetCoun??? 1??? ???
        int[] dx = {-2, -1, -1, -1, 0, 0, 0, 0, 1, 1, 1, 2};
        int[] dy = {0, -1, 0, 1, -2, -1, 1, 2, -1, 0, 1, 0};

        for(int i = 0; i < carpetZone.length; i++){
            if(!(x + dx[i] < 0 || x + dx[i] > 6 || y + dy[i] < 0 || y + dy[i] > 6)){
                // ?????? ?????? ?????? 2??? ?????? ???????????? ??? ?????????
                if(carpetSquare[x + dx[i]][y + dy[i]].getComponentCount() == 3 &&
                carpetSquare[x + dx[i]][y + dy[i]].getComponent(2).getBackground() == removedColor){
                    carpetSquare[x + dx[i]][y + dy[i]].getComponent(1).setVisible(false);
                }

                String sfx = firstCarpetOwner.substring(0, 1);
                String sfy = firstCarpetOwner.substring(1, 2);
                int fx = Integer.parseInt(sfx);
                int fy = Integer.parseInt(sfy);

                // ???????????? ???????????? ????????? ??? ?????????
                if(!(x + dx[i] == fx && y + dy[i] == fy + 1 ||
                x + dx[i] == fx && y + dy[i] == fy - 1 ||
                x + dx[i] == fx + 1 && y + dy[i] == fy ||
                x + dx[i] == fx - 1 && y + dy[i] == fy)){
                    carpetZone[i].setVisible(false);
                }
            }
        }
    }

    public void setPossibleZoneVisible(){
        for(int i = 0; i < carpetZone.length; i++){
            carpetZone[i].setVisible(true);
        }
    }

    public void setCarpetZoneUnvisible(){
        for(int i = 0; i < carpetZone.length; i++){
            carpetZone[i].setVisible(false);
        }
    }
    
    public void removeZone(int x, int y){
        int[] dx = {-2, -1, -1, -1, 0, 0, 0, 0, 1, 1, 1, 2};
        int[] dy = {0, -1, 0, 1, -2, -1, 1, 2, -1, 0, 1, 0};

        for(int i = 0; i < carpetZone.length; i++){
            if(!(x + dx[i] < 0 || x + dx[i] > 6 || y + dy[i] < 0 || y + dy[i] > 6)){
                carpetSquare[x + dx[i]][y + dy[i]].remove(carpetZone[i]);
            }
        }
    }

    class BoardPane extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(icon.getImage(), 0, 0, 350, 350, null);
        }
    }
    
    public void createInfo(){
        infoPane = new JPanel();
        infoPane.setLayout(new BorderLayout());
        infoPane.setBorder(new EmptyBorder(50, 0, 50, 50));

        //??????????????? ??????, ?????? ?????? ????????? ???????????? ??????
        infoPartPane = new JPanel();
        infoPartPane.setLayout(new GridLayout(3, 1, 0, 15));

        // player1, player2 ?????? ??????
        infoPane1 = new JPanel();
        infoPane1.setLayout(new GridLayout(2, 1, 0, 15));

        //player1??? ??????
        player1 = new JPanel();
        player1.setLayout(new BorderLayout());
        player1.setBackground(Color.LIGHT_GRAY);

        stateLabel[0] = new JLabel("Player1");
        stateLabel[0].setFont(new Font("Arial", Font.BOLD, 20));
        stateLabel[0].setHorizontalAlignment(JLabel.CENTER);        

        //player2
        player2 = new JPanel();
        player2.setLayout(new BorderLayout());

        player2.setBackground(Color.LIGHT_GRAY);
        stateLabel[1] = new JLabel("Player2");
        stateLabel[1].setFont(new Font("Arial", Font.BOLD, 20));
        stateLabel[1].setHorizontalAlignment(JLabel.CENTER);

        infoPane2 = new JLabel();
        infoPane2.setLayout(new GridLayout(2, 1, 0, 15));

        //player3
        player3 = new JPanel();
        player3.setLayout(new BorderLayout());
        player3.setBackground(Color.LIGHT_GRAY);

        stateLabel[2] = new JLabel("Player3");
        stateLabel[2].setFont(new Font("Arial", Font.BOLD, 20));
        stateLabel[2].setHorizontalAlignment(JLabel.CENTER);
        
        //player4
        player4 = new JPanel();
        player4.setLayout(new BorderLayout());
        player4.setBackground(Color.LIGHT_GRAY);

        stateLabel[3] = new JLabel("Player4");
        stateLabel[3].setFont(new Font("Arial", Font.BOLD, 20));
        stateLabel[3].setHorizontalAlignment(JLabel.CENTER);


        player1.add(stateLabel[0]);        
        infoPane1.add(player1);

        player2.add(stateLabel[1]);
        infoPane1.add(player2);

        player3.add(stateLabel[2]);
        infoPane2.add(player3);

        player4.add(stateLabel[3]);
        infoPane2.add(player4);

        //log ???
        infoPane3 = new JPanel();
        //append??? area??? ?????? ?????????
        logText = new JTextArea("Log", 7, 28);        
        infoPane3.add(new JScrollPane(logText));

        //player1, 2
        infoPartPane.add(infoPane1);
        //player3, 4
        infoPartPane.add(infoPane2);
        //game log
        infoPartPane.add(infoPane3);

        infoPane.add(infoPartPane);
        add(infoPane);
    }

    // ?????? ?????? ??????
    // ????????? ?????? ????????? ?????? ????????? ?????????
    // x++, y++ ??? ?????? ????????? ????????? ????????? ?????? ?????? ?????? ?????? ??????
    public int throwingDice(){
        System.out.println("????????? ?????????");
        int[] dice = {1, 2, 2, 3, 3, 4};
        int surface = (int)(Math.random() * 6);

        if(isCpuPlaying == true){
            logText.append("CPU??? ???????????? ????????????." + '\n');    
        }
        else{
            logText.append("???????????? ????????????." + '\n');
        }
            
        logText.append(dice[surface] + "???(???) ???????????????." + '\n');
        logText.setCaretPosition(logText.getDocument().getLength());

        if(isOnline == true){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("SETTHROWING-" + dice[surface]);            
            }
            catch(IOException er){
                System.out.println(er.getMessage());
            }
        }
        return dice[surface];
    }

    public synchronized void moving(int dice){        
        timing tm = new timing(dice);
        tm.start();
    }
    
    class timing extends Thread{
        int dice;
        public timing(int dice){
            this.dice = dice;
        }
        public void run(){
            int n = 0;
            isMoving = true;
            removeZone(checker.getRow(), checker.getColumn());            
    
            while(n < dice){
                if(checker.getDirection().equals("south") && n < dice){
                    goSouth(checker.getRow(), checker.getColumn());
                    n += 1;
                    repaint();
                    try{
                        sleep(1000);
                    }
                    catch(InterruptedException e){
                        return;
                    }
                }
                if(checker.getDirection().equals("north") && n < dice){
                    goNorth(checker.getRow(), checker.getColumn());
                    n += 1;
                    repaint();
                    try{
                        sleep(1000);
                    }
                    catch(InterruptedException e){
                        return;
                    }
                }
                if(checker.getDirection().equals("east") && n < dice){
                    goEast(checker.getRow(), checker.getColumn());
                    n += 1;
                    repaint();
                    try{
                        sleep(1000);
                    }
                    catch(InterruptedException e){
                        return;
                    }
                }
                if(checker.getDirection().equals("west") && n < dice){
                    goWest(checker.getRow(), checker.getColumn());
                    n += 1;
                    repaint();
                    try{
                        sleep(1000);
                    }
                    catch(InterruptedException e){
                        return;
                    }
                }                
            }
            isMoving = false;
            marker.setIcon(checker.getChecker(checker.getDirection()));
            
            // ???????????? ???????????? ????????? ???
            if(carpetSquare[checker.getRow()][checker.getColumn()].getComponentCount() == 2 &&
            !(carpetSquare[checker.getRow()][checker.getColumn()].getComponent(1).getName().equals(Integer.toString(turn + 1)))){
                steppedOnCarpet(Integer.parseInt(carpetSquare[checker.getRow()][checker.getColumn()].getComponent(1).getName()));
            }
            // ???????????? ???????????? ?????? ????????????
            else{                              
                if(isOnline == false || myTurn == true){                    
                    possibleZone(checker.getRow(), checker.getColumn());
                    logText.append("???????????? ??????????????????." + '\n');
                    logText.setCaretPosition(logText.getDocument().getLength());
                }
                if(isCpu == true && turn == 1){                    
                    System.out.println("?????? ??? ??????");
                    setCpuCarpeting();                    
                }                
            }
            revalidate();
            repaint();
        }
    }

    public void goSouth(int x, int y){
        marker.setIcon(checker.southChecker());
        
        x++;
        if(x == 7){
            if(y == 6){
                checker.setDirection("west");
            }
            else{
                checker.setDirection("north");
                if(y == 0 || y == 2 || y == 4){
                    checker.setColumn(y + 1);
                    square[x - 1][checker.getColumn()].add(marker);
                }
                else if(y == 1 || y == 3 || y == 5){
                    checker.setColumn(y - 1);
                    square[x - 1][checker.getColumn()].add(marker);
                }
            }
            return;
        }
    
        square[x][y].add(marker);
        
        checker.setRow(x);
        checker.setColumn(y);        
    
        return;
    }

    public void goNorth(int x, int y){
        marker.setIcon(checker.northChecker());
        x--;
        if(x == -1){
            if(y == 0){
                checker.setDirection("east");
            }
            else{
                checker.setDirection("south");
                if(y == 2 || y == 4 || y == 6){
                    checker.setColumn(y - 1);
                    square[x + 1][checker.getColumn()].add(marker);
                }
                else if(y == 1 || y == 3 || y == 5){
                    checker.setColumn(y + 1);
                    square[x + 1][checker.getColumn()].add(marker);
                }
            }
            return;
        }
    
        square[x][y].add(marker);
        
        checker.setRow(x);
        checker.setColumn(y);        
    
        return;
    }

    public void goEast(int x, int y){
        marker.setIcon(checker.eastChecker());
    
        y++;
    
        if(y == 7){
            if(x == 6){
                checker.setDirection("north");
            }
            else{
                checker.setDirection("west");
                if(x == 0 || x == 2 || x == 4){
                    checker.setRow(x + 1);
                    square[checker.getRow()][y - 1].add(marker);
                }
                else if(x == 1 || x == 3 || x == 5){
                    checker.setRow(x - 1);
                    square[checker.getRow()][y - 1].add(marker);
                }
            }
            return;
        }
    
        square[x][y].add(marker);
        
        checker.setRow(x);
        checker.setColumn(y);        

        return;
    }

    public void goWest(int x, int y){
        marker.setIcon(checker.westChecker());
    
        y--;
    
        if(y == -1){
            if(x == 0){
                checker.setDirection("south");
            }
            else{
                checker.setDirection("east");
                if(x == 2 || x == 4 || x == 6){
                    checker.setRow(x - 1);
                    square[checker.getRow()][y + 1].add(marker);
                }
                else if(x == 1 || x == 3 || x == 5){
                    checker.setRow(x + 1);
                    square[checker.getRow()][y + 1].add(marker);
                }
            }
            return;
        }
    
        square[x][y].add(marker);
    
        checker.setRow(x);
        checker.setColumn(y);

        return;
    }
    // ?????? ?????? ?????? ??????
    
    // ????????? ?????????, ?????? ????????? ????????? ?????? ?????????
    class ControlerWindow extends JFrame {
        private final int WIDTH = 300;
        private final int HEIGHT = 300;        

        public ControlerWindow(){
            setTitle("Controler");
            Container c = getContentPane();
            c.setLayout(new GridLayout(1, 1));

            throwingDiceBtn = new JButton("????????? ?????????");
            throwingDiceBtn.setEnabled(false);

            throwingDiceBtn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    moving(throwingDice());
                    throwingDiceBtn.setEnabled(false);
                }
            });

            throwingDiceBtn.addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent e){
                    if(throwingDiceBtn.isEnabled() == false){
                        logText.append("????????? ?????? ??????????????????!" + '\n');
                    }
                }
            });

            passPlayerTurnBtn = new JButton("?????? ?????????");                                    
            passPlayerTurnBtn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    logText.append("????????? ????????????." + '\n');
                    if(carpetCount == 0){
                        passTurn();
                    }
                    else if(carpetCount == 1){
                        carpetCount++;
                    }
                }
            });
            
            c.add(throwingDiceBtn);
            c.add(passPlayerTurnBtn);
    
            setSize(WIDTH, HEIGHT);
            setVisible(true);
        }
    }
     
    // ????????? ????????? ????????? ?????????
    public void start(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            // @Override
            public void run(){
                if(isRunning == true){
                    for(int i = 0; i < playerNum; i++){
                        if(player[i].getIsAlive() == true){
                            if(playerNum == 2){                                
                                stateLabel[i].setText("?????????: " + player[i].getTotalDirham() + ", "
                                        + "?????????: " + player[i].getCarpet() + " " 
                                        + "(" + colorNames[player[i].getColor().get(0)]
                                        + ", " + colorNames[player[i].getColor().get(1)] + ")");
                            }
                            else{
                                stateLabel[i].setText("?????????: " + player[i].getTotalDirham() + ", "
                                            + "?????????: " + player[i].getCarpet() + " " 
                                            + "(" + colorNames[player[i].getColor().get(0)] + ")");
                            }
                        }
                        else{
                            stateLabel[i].setText("Player" + (i + 1) + " ??????");
                        }
                    }
                    if(turn == 1 && isCpu == true && isCpuPlaying == false){                        
                        isCpuPlaying = true;
                        playWithCpu();                        
                    }
                    if(isCpu == true){
                        if(isCarpetLeft() && nowPlaying(turn).getCarpet() == 0){
                            logText.append("???????????? ?????? ???????????? ?????? ???????????????." + '\n');
                            passTurn();
                            if(turn == 0){
                                setDirectionArrowUnvisible();
                            }                            
                            logText.setCaretPosition(logText.getDocument().getLength());
                        }
                    }

                    if(isMoving == true || isOnline == true){
                        passPlayerTurnBtn.setEnabled(false);
                    }
                    else{
                        passPlayerTurnBtn.setEnabled(true);
                    }

                    if(carpetCount == 0 && canceled){
                        setPossibleZoneVisible();
                        canceled = false;
                    }
    
                    // ??????????????? 3?????? ?????? ????????? ?????? 1??? ?????? ??????
                    if(carpetCount == 1){                        
                        if((nowPlaying(turn).getCarpet() == 0)){
                            carpetCount++;
                            setCarpetZoneUnvisible();                            
                        }
                        else{
                            secondPossibleZone(checker.getRow(), checker.getColumn());                            
                        }
                    }
                    
                    // turn endpoint
                    if(carpetCount >= 2){
                        turn++;
                        
                        if(turn >= player.length){
                            turn = 0;
                        }
                        if(myTurn == true){                            
                            try{
                                writer = new PrintWriter(socket.getOutputStream(), true);
                                writer.println("CHECK-");                                                                
                            }
                            catch(IOException e){
                                System.out.println(e.getMessage());
                            }
                            myTurn = false;                            
                        }                        
                        if(isCpu == true && isCpuPlaying == true){
                            isCpuPlaying = false;
                        }
                        if(player[turn].getIsAlive() == false){
                            while(player[turn].getIsAlive() == false){
                                turn++;
                                if(turn >= player.length){
                                    turn = 0;
                                }
                            }
                        }
                        carpetCount = 0;

                        // 1?????? ???????????? ??? ?????? ??????
                        if(isCarpetLeft()){
                            if(isOnline == false){
                                logText.append((turn + 1) + "?????? ???????????????." + "\n");
                                logText.append("????????? ???????????????." + '\n');
                                logText.setCaretPosition(logText.getDocument().getLength());
                                if(!(isCpu == true && turn == 1))
                                {
                                    setDirectionArrowVisible();
                                }
                            }
                        }
                        else{
                            logText.append("?????? ??????????????? ???????????? ??????????????????." + '\n');
                            logText.append("????????? ???????????? ????????? ???????????????." + '\n');
                            logText.setCaretPosition(logText.getDocument().getLength());
                            computeGameResult();                            
                        }      
                    }
                }
                else{
                    System.out.println("??????");
                    cancel();
                }                    
            }
        };
        timer.schedule(task, 1000, 100);
    }

    // ?????? ?????? ??????
    class gameMode extends JFrame{
        private final int WIDTH = 300;
        private final int HEIGHT = 300;
        public gameMode(){
            setTitle("?????? ?????? ????????????");
            Container c = getContentPane();
            c.setLayout(new GridLayout(3, 1));

            justStartBtn = new JButton("????????????");            
            gameStartWithCpuBtn = new JButton("????????? ??????");
            gameStartOnlineBtn = new JButton("????????? ??????");            

            justStartBtn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    num = JOptionPane.showInputDialog("??? ?????? ???????????????????(2 ~ 4???)");
                    int input = Integer.parseInt(num);
                    if(!(input >= 2 && input <= 4)){
                        while(!(input >= 2 && input <= 4)){
                            System.out.println("??????");
                            num = JOptionPane.showInputDialog("??? ?????? ???????????????????(2 ~ 4???)");
                            input = Integer.parseInt(num);
                        }
                    }
                    if(input >= 2 && input <= 4){
                        System.out.println("??????");
                        isRunning = true;
                        playerNum = Integer.parseInt(num);
                        player = new Player[playerNum];
                        startGame();
                        dispose();
                    }
                }
            });

            gameStartWithCpuBtn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    num = Integer.toString(2);
                    isRunning = true;
                    playerNum = Integer.parseInt(num);
                    player = new Player[playerNum];
                    isCpu = true;
                    startGame();
                    dispose();
                }
            });

            gameStartOnlineBtn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new OnlineMode();
                    dispose();
                }
            });

            c.add(justStartBtn);
            c.add(gameStartWithCpuBtn);
            c.add(gameStartOnlineBtn);

            setResizable(false);
            setSize(WIDTH, HEIGHT);
            setVisible(true);
        }
    }
    class OnlineMode extends JFrame{
        private final int WIDTH = 200;
        private final int HEIGHT = 200;
        int input;

        public OnlineMode(){
            setTitle("????????? ??????");
            Container c = getContentPane();
            c.setLayout(new GridLayout(2, 1));

            newGameBtn = new JButton("?????? ??????");            
            joinGameBtn = new JButton("?????? ??????");

            newGameBtn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    num = JOptionPane.showInputDialog("??? ?????? ???????????????????(2 ~ 4???)");
                    input = Integer.parseInt(num);
                    if(!(input >= 2 && input <= 4)){
                        while(!(input >= 2 && input <= 4)){
                            System.out.println("??????");
                            num = JOptionPane.showInputDialog("??? ?????? ???????????????????(2 ~ 4???)");
                            input = Integer.parseInt(num);
                        }
                    }
                    if(input >= 2 && input <= 4){
                        Runnable r = new StartServer(input);
                        Thread serverThread = new Thread(r);
                        new Serverform(input);
                        serverThread.start();
                        isOnline = true;
                        dispose();
                    }
                }
            });

            joinGameBtn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new StartClient();
                    isOnline = true;
                    dispose();
                }
            });

            c.add(newGameBtn);
            c.add(joinGameBtn);

            setResizable(false);
            setSize(WIDTH, HEIGHT);
            setVisible(true);
        }

    }

    class Serverform extends JFrame{
        private final int WIDTH = 300;
        private final int HEIGHT = 300;
        public Serverform(){
        }
        
        public Serverform(int input){
            setTitle("?????? ?????? ???");
            Container c = getContentPane();
            c.setLayout(new GridLayout(1, 1));

            connectedState = new JTextArea(8, 1);            

            c.add(connectedState);

            setResizable(false);
            setSize(WIDTH, HEIGHT);
            setVisible(true);
        }
    }

    class StartServer implements Runnable{
        private ArrayList<OnlineClient> clients;
        private ServerSocket serverSocket;        
        private int PORT = 9999;
        private int input;

        public StartServer(){

        }

        public StartServer(int input){
            this.input = input;
        }

        public void run(){
            try{
                serverSocket = new ServerSocket(PORT);
                clients = new ArrayList<>();

                System.out.println("?????? ?????? ????????????.");
                while(true){
                    connectedState.append((input - (clients.size() + 1)) + "?????? ????????? ?????? ????????????..." + '\n');                   
                    socket = serverSocket.accept();                    
    
                    OnlineClient client = new OnlineClient(socket);
                    client.start();
                    clients.add(client);
                    
                    if(clients.size() == input - 1){
                        isRunning = true;
                        playerNum = input;
                        player = new Player[playerNum];
                        startGame();
                        setDirectionArrowVisible();                                               
                        
                        break;                        
                    }
                }
            }
            catch(Exception e){
                System.out.println("?????? ??????");
                System.out.println(e.getMessage());
            }
        }

        class OnlineClient extends Thread{
            private Socket socket;
            private PrintWriter writer;
            private BufferedReader reader;
            private int playerTurn = 0;
            private boolean isStart = false;
            private boolean assigned = false;

            public OnlineClient(Socket socket){
                this.socket = socket;
            }

            // ?????????????????? ?????? ?????????
            public void run(){
                try{
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    connectedState.append((clients.size() + 1) + "??? ???????????? ?????? ??????" + '\n');
                    
                    String readerMsg = null;
                    String[] parsReaderMsg;                    
                    while((readerMsg = reader.readLine()) != null){
                        parsReaderMsg = readerMsg.split("-");
                        //?????? ????????????
                        protocolStart();
                        protocolAssignTurn();
                        protocolTurn(parsReaderMsg);
                        protocolTurnForClients(parsReaderMsg);
                        protocolSetDirection(parsReaderMsg);
                        protocolSetDirectionForClient(parsReaderMsg);
                        protocolSetCarpeting(parsReaderMsg);
                        protocolSetCarpetingForClients(parsReaderMsg);
                        protocolSetCarpetingOnTop(parsReaderMsg);
                        protocolSetCarpetingOnTopForClients(parsReaderMsg);
                        protocolSetCancelCarpeting(parsReaderMsg);
                        protocolSetCancelCarpetingForClients(parsReaderMsg);
                        protocolSetCancelCarpetingOnTop(parsReaderMsg);
                        protocolSetCancelCarpetingOnTopForClients(parsReaderMsg);
                        protocolSetThrowing(parsReaderMsg);
                        protocolSetThrowingForClients(parsReaderMsg);                        
                    }
                }
                catch(Exception e){
                    System.out.println("?????? ??????");
                    System.out.println(e.getMessage());
                }
            }

            public void protocolStart(){                
                if(isStart == false && clients.size() == (input - 1)){
                    for(int i = 0; i < clients.size(); i++){                        
                        clients.get(i).isStart = true;
                        clients.get(i).writer.println("START-" + input);
                    }
                    
                    if(turn == 0){                        
                        for(int j = 0; j < clients.size(); j++){
                            clients.get(j).writer.println("NOTTURN-");
                        }
                        logText.append("????????? ???????????????" + '\n');
                        myTurn = true;
                        isHostTurn = true;
                        for(int j = 0; j < clients.size(); j++){
                            clients.get(j).writer.println("SERVER-Player1?????? ???????????????.");
                        }
                    }
                }                
            }

            public void protocolAssignTurn(){
                if(isStart == true && assigned == false){
                    for(int i = 0; i < clients.size(); i++){
                        clients.get(i).playerTurn = playerTurn;
                        clients.get(i).writer.println("ASSIGNED-" + (playerTurn + 2));
                        clients.get(i).assigned = true;
                                         
                        if(i != clients.size() - 1){
                            playerTurn++;
                        }
                    }
                }
            }            

            // improved ver
            public void protocolTurn(String[] msg){
                if(isHostTurn == true && msg[0].equalsIgnoreCase("NEXT")){                                        
                    for(int i = 0; i < clients.size(); i++){                        
                        if(clients.get(i).playerTurn + 1 == turn){ // playerturn = 0,1,2                            
                            for(int j = 0; j < clients.size(); j++){
                                clients.get(j).writer.println("NOTTURN-");                                
                                clients.get(j).writer.println("TURNADD-");  // ?????? ??? ?????? ?????????                                
                            }
                            try{
                                Thread.sleep(100);
                                clients.get(i).writer.println("TURN-");
                            }
                            catch(Exception e){
                                System.out.println(e.getMessage());
                            }                            
                                                                                  
                            for(int j = 0; j < clients.size(); j++){
                                if(j == i){
                                    clients.get(j).writer.println("SERVER-????????? ???????????????.");
                                }
                                else{
                                    clients.get(j).writer.println("SERVER-Player" + (clients.get(i).playerTurn+2) + 
                                    "??? ???????????????.");        
                                }
                            }
                            logText.append("Player" + (turn + 1) + "?????? ???????????????." + '\n');
                            setDirectionArrowUnvisible();
                            isHostTurn = false;
                            isClientTurn = true;
                            logText.setCaretPosition(logText.getDocument().getLength());
                        }
                    }                    
                }
            }

            // improved ver
            public void protocolTurnForClients(String[] msg){                              
                if(isClientTurn == true && msg[0].equalsIgnoreCase("CHECK")){                    
                    passTurn();
                    try{
                        Thread.sleep(500);                        
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    for(int i = 0; i < clients.size(); i++){                        
                        if((clients.get(i).playerTurn + 1) == turn){ // playerturn = 0,1,2
                            if(i == 1){
                                for(int j = 1; j < clients.size(); j++){
                                    // playerturn 0 ?????? ?????? turnadd
                                    clients.get(j).writer.println("TURNADD-");                                    
                                }
                            }
                            else if(i == 2){
                                // playerturn 1 ?????? ?????? turnadd
                                clients.get(0).writer.println("TURNADD-");
                                clients.get(2).writer.println("TURNADD-");                                                                    
                            }
                            try{
                                Thread.sleep(100);
                                clients.get(i).writer.println("TURN-");
                            }
                            catch(Exception e){
                                System.out.println(e.getMessage());
                            }
                            for(int j = 0; j < clients.size(); j++){                    
                                if(j == i){
                                    clients.get(j).writer.println("SERVER-????????? ???????????????.");
                                }
                                else{
                                    clients.get(j).writer.println("NOTTURN-");
                                    clients.get(j).writer.println("SERVER-Player" + (clients.get(i).playerTurn+2) + 
                                    "??? ???????????????.");        
                                }
                            }
                            logText.append("Player" + (turn + 1) + "?????? ???????????????." + '\n');
                            setDirectionArrowUnvisible();
                            logText.setCaretPosition(logText.getDocument().getLength());
                        }
                    }

                    // ?????? ?????? ????????? ?????? ?????? ?????? ???????????? ??? ??????
                    if(turn == 0 && isCarpetLeft() == true){      
                        isHostTurn = true;
                        isClientTurn = false;
                        for(int j = 0; j < clients.size(); j++){
                            clients.get(j).writer.println("NOTTURN-");
                            if(j != (clients.size() - 1)){
                                clients.get(j).writer.println("TURNADD-");                                
                            }                                                        
                        }
                        logText.append("????????? ???????????????" + '\n');
                        myTurn = true;
                        setDirectionArrowVisible();
                        for(int j = 0; j < clients.size(); j++){
                            clients.get(j).writer.println("SERVER-Player1?????? ???????????????.");
                        }
                        logText.setCaretPosition(logText.getDocument().getLength());
                    }
                    // ?????? ?????? ??? clients??? ?????? ?????? ?????? turn add
                    else if(turn == 0 && isCarpetLeft() == false){
                        if(clients.size() == 2){
                            clients.get(0).writer.println("TURNADD-");
                        }
                        else if(clients.size() == 3){
                            clients.get(0).writer.println("TURNADD-");
                            clients.get(1).writer.println("TURNADD-");
                        }
                    }
                }
            }

            
            // ?????? ?????? ?????? ???????????? ???????????? ????????????????????? ?????????
            // ????????????????????? ????????? ???????????? ?????????
            // ???????????? ?????????????????? ????????? ???????????? ????????? ??????!
            public void protocolSetDirection(String[] msg){                
                if(msg[0].equalsIgnoreCase("DIRECTION")){
                    for(int i = 0; i < clients.size(); i++){
                        clients.get(i).writer.println("SENDDIRECTION-" + msg[1]);
                    }
                    checker.setDirection(msg[1]);
                    marker.setIcon(checker.getChecker(checker.getDirection()));
                }
            }

            // ?????????????????? ?????? ???????????? ?????? ????????? ??????
            // ????????? host ?????? ????????? ????????? ?????????????????? ?????? ?????? ?????????
            public void protocolSetDirectionForClient(String[] msg){
                if(msg[0].equalsIgnoreCase("SETDIRECTION")){                    
                    for(int i = 0; i < clients.size(); i++){
                        clients.get(i).writer.println("SENDDIRECTION-" + msg[1]);
                    }
                    checker.setDirection(msg[1]);
                    marker.setIcon(checker.getChecker(checker.getDirection()));
                }
            }

            public void protocolSetCarpeting(String[] msg){
                if(msg[0].equalsIgnoreCase("CARPETING")){
                    for(int i = 0; i < clients.size(); i++){                        
                        clients.get(i).writer.println("SENDCARPETING-" + msg[1] + "-" + msg[2] + "-" + msg[3] + "-" + msg[4] + "-" + msg[5]);                                                
                    }
                }
            }

            public void protocolSetCarpetingForClients(String[] msg){
                if(msg[0].equalsIgnoreCase("SETCARPETING")){
                    for(int i = 0; i < clients.size(); i++){                        
                        clients.get(i).writer.println("SENDCARPETING-" + msg[1] + "-" + msg[2] + "-" + msg[3] + "-" + msg[4] + "-" + msg[5]);
                    }
                    player[Integer.parseInt(msg[1])].setCarpet();

                    String tmpX = msg[2].substring(0, 1);
                    String tmpY= msg[2].substring(1, 2);
                    int x = Integer.parseInt(tmpX);
                    int y = Integer.parseInt(tmpY);
                    int r = Integer.parseInt(msg[3]);
                    int g = Integer.parseInt(msg[4]);
                    int b = Integer.parseInt(msg[5]);                    
                    
                    Color otherColor = getCarpetColor(r, g, b);

                    JLabel otherCarpet = new JLabel();
                    otherCarpet.setBackground(otherColor);
                    otherCarpet.setVisible(true);
                    otherCarpet.setOpaque(true);                    
                    otherCarpet.setName(Integer.toString(turn + 1));                                       
                    carpetSquare[x][y].add(otherCarpet);                    

                    repaint();
                    revalidate();
                }
            }

            public void protocolSetCarpetingOnTop(String[] msg){
                if(msg[0].equalsIgnoreCase("CARPETINGONTOP")){
                    for(int i = 0; i < clients.size(); i++){                        
                        clients.get(i).writer.println("SENDCARPETINGONTOP-" + msg[1] + "-" + msg[2] + "-" + msg[3] + "-" + msg[4] + "-" + msg[5]);
                    }
                }
            }

            public void protocolSetCarpetingOnTopForClients(String[] msg){
                if(msg[0].equalsIgnoreCase("SETCARPETINGONTOP")){
                    for(int i = 0; i < clients.size(); i++){                        
                        clients.get(i).writer.println("SENDCARPETINGONTOP-" + msg[1] + "-" + msg[2] + "-" + msg[3] + "-" + msg[4] + "-" + msg[5]);                        
                    }
                    player[Integer.parseInt(msg[1])].setCarpet();
                                
                    String tmpX = msg[2].substring(0, 1);
                    String tmpY = msg[2].substring(1, 2);
                    int x = Integer.parseInt(tmpX);
                    int y = Integer.parseInt(tmpY);
                    int r = Integer.parseInt(msg[3]);
                    int g = Integer.parseInt(msg[4]);
                    int b = Integer.parseInt(msg[5]);

                    carpetSquare[x][y].remove(carpetSquare[x][y].getComponent(1));
                    
                    Color otherColor = getCarpetColor(r, g, b);

                    JLabel otherCarpet = new JLabel();
                    otherCarpet.setBackground(otherColor);
                    otherCarpet.setVisible(true);
                    otherCarpet.setOpaque(true);
                    otherCarpet.setName(Integer.toString(turn + 1));                    
                    carpetSquare[x][y].add(otherCarpet);

                    repaint();
                    revalidate();
                }
            }

            public void protocolSetCancelCarpeting(String[] msg){
                if(msg[0].equalsIgnoreCase("CANCELCARPETING")){
                    for(int i = 0; i < clients.size(); i++){
                        clients.get(i).writer.println("SENDCANCELCARPETING-" + msg[1] + "-" + msg[2]);
                    }
                }
            }

            public void protocolSetCancelCarpetingForClients(String[] msg){
                if(msg[0].equalsIgnoreCase("SETCANCELCARPETING")){
                    for(int i = 0; i < clients.size(); i++){
                        clients.get(i).writer.println("SENDCANCELCARPETING-" + msg[1] + "-" + msg[2]);
                    }
                    String tmpX = msg[2].substring(0, 1);
                    String tmpY = msg[2].substring(1, 2);
                    int x = Integer.parseInt(tmpX);
                    int y = Integer.parseInt(tmpY);                                                                

                    carpetSquare[x][y].remove(carpetSquare[x][y].getComponent(1));                                
                    player[Integer.parseInt(msg[1])].canceledCarpet();

                    revalidate();
                    repaint();                    
                }
            }

            public void protocolSetCancelCarpetingOnTop(String[] msg){
                if(msg[0].equalsIgnoreCase("CANCELCARPETINGONTOP")){
                    for(int i = 0; i < clients.size(); i++){
                        clients.get(i).writer.println("SENDCANCELCARPETINGONTOP-" + msg[1] + "-" + msg[2] + "-" + msg[3] + 
                        "-" + msg[4] + "-" + msg[5] + "-" + msg[6] + "-" + msg[7] + "-" + msg[8]);
                    }
                }
            }

            public void protocolSetCancelCarpetingOnTopForClients(String[] msg){
                if(msg[0].equalsIgnoreCase("SETCANCELCARPETINGONTOP")){
                    for(int i = 0; i < clients.size(); i++){
                        clients.get(i).writer.println("SENDCANCELCARPETINGONTOP-" + msg[1] + "-" + msg[2] + "-" + msg[3] + 
                        "-" + msg[4] + "-" + msg[5] + "-" + msg[6] + "-" + msg[7] + "-" + msg[8]);
                    }
                    String tmpX = msg[2].substring(0, 1);
                    String tmpY = msg[2].substring(1, 2);
                    int x = Integer.parseInt(tmpX);
                    int y = Integer.parseInt(tmpY);
                    int nx = Integer.parseInt(msg[3]);
                    int ny = Integer.parseInt(msg[4]);
                    int r = Integer.parseInt(msg[6]);
                    int g = Integer.parseInt(msg[7]);
                    int b = Integer.parseInt(msg[8]);
                    JLabel removedCarpet = new JLabel();
                    Color removedCarpetColor = getCarpetColor(r, g, b);

                    carpetSquare[x][y].remove(carpetSquare[x][y].getComponent(1));
                    removedCarpet.setName(msg[5]);
                    removedCarpet.setBackground(removedCarpetColor); 
                    
                    carpetSquare[nx][ny].add(removedCarpet);
                    removedCarpet.setVisible(true);
                    removedCarpet.setOpaque(true);
                    player[Integer.parseInt(msg[1])].canceledCarpet();

                    repaint();
                    revalidate();
                }                
            }

            public void protocolSetThrowing(String[] msg){
                if(msg[0].equalsIgnoreCase("THROWING")){
                    for(int i = 0; i < clients.size(); i++){                        
                        clients.get(i).writer.println("SENDTHROWING-" + msg[1]);                        
                    }
                }
            }

            public void protocolSetThrowingForClients(String[] msg){
                if(msg[0].equalsIgnoreCase("SETTHROWING")){
                    for(int i = 0; i < clients.size(); i++){                        
                        clients.get(i).writer.println("SENDTHROWING-" + msg[1]);                        
                    }
                    moving(Integer.parseInt(msg[1]));
                }
            }
        }
    }

    class StartClient{        

        public StartClient(){
            connectServer();
            initMsg();
            sendReady();
        }

        public void connectServer(){
            try{
                socket = new Socket("localhost", 9999);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                Reader r = new Reader();
                r.start();
                System.out.println("??????");
            }
            catch(Exception e){
                System.out.println("?????? ??????");
                System.out.println(e.getMessage());
            }
        }

        public void initMsg(){
            logText.append(" ???????????????." + '\n');
        }

        public void sendReady(){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("READY-");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        public void checkTurn(){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("NEXT-");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        public void sendDirection(String direction){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("DIRECTION-" + direction);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        public void carpeting(String turn, String where, String red, String green, String blue){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("CARPETING-" + turn + "-" + where + "-" + red + "-" + green + "-" + blue);                
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        public void carpetingOnTop(String turn, String where, String red, String green, String blue){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("CARPETINGONTOP-" + turn + "-" + where + "-" + red + "-" + green + "-" + blue);                
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        public void cancelCarpet(String turn, String where){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("CANCELCARPETING-" + turn + "-" + where);                
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        public void cancelCarpetOnTop(String turn, String where, String x, String y, String owner, String r, String g, String b){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("CANCELCARPETINGONTOP-" + turn + "-" + where + "-" + x + "-" + y +
                "-" + owner + "-" + r + "-" + g + "-" + b);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        public void throwing(String surface){
            try{
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("THROWING-" + surface);                
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        class Reader extends Thread{
            // ??????????????? protocol ?????? ????????? ?????? ????????????
            public void run(){
                try{
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = null;
                    String[] parsLine;

                    while((line = reader.readLine()) != null){
                        parsLine = line.split("-");
                        if(parsLine[0].equalsIgnoreCase("START")){
                            isRunning = true;
                            int input = Integer.parseInt(parsLine[1]);
                            playerNum = input;
                            player = new Player[playerNum];                                                        
                            startGame();      
                            setDirectionArrowVisible();
                            try{
                                Thread.sleep(100);
                                setDirectionArrowUnvisible();
                            }
                            catch(Exception e){
                                System.out.println(e.getMessage());
                            }
                        }
                        else if(parsLine[0].equalsIgnoreCase("ASSIGNED")){
                            logText.append("????????? player" + parsLine[1] + "????????????." + '\n');
                        }
                        else if(parsLine[0].equalsIgnoreCase("SERVER")){
                            logText.append(parsLine[1] + '\n');
                            logText.setCaretPosition(logText.getDocument().getLength());
                        }
                        else if(parsLine[0].equalsIgnoreCase("NOTTURN")){                            
                            setDirectionArrowUnvisible();
                        }
                        else if(parsLine[0].equalsIgnoreCase("TURNADD")){                            
                            passTurn();
                        }
                        else if(parsLine[0].equalsIgnoreCase("TURN")){                            
                            setDirectionArrowVisible();
                            myTurn = true;                                                        
                        }                        
                        else if(parsLine[0].equalsIgnoreCase("CHECK")){                        
                            checkTurn();
                        }
                        else if(parsLine[0].equalsIgnoreCase("SETDIRECTION")){                            
                            sendDirection(parsLine[1]);                            
                        }
                        else if(parsLine[0].equalsIgnoreCase("SENDDIRECTION")){
                            checker.setDirection(parsLine[1]);
                            marker.setIcon(checker.getChecker(checker.getDirection()));
                        }
                        // ????????? ??? ?????? ??????(for host)
                        else if(parsLine[0].equalsIgnoreCase("SETCARPETING")){                            
                            carpeting(parsLine[1], parsLine[2], parsLine[3], parsLine[4], parsLine[5]);
                        }
                        // ????????? ??? ?????? ??????(for host and clients)
                        else if(parsLine[0].equalsIgnoreCase("SENDCARPETING")){                                                        
                            if(myTurn == false){
                                player[Integer.parseInt(parsLine[1])].setCarpet();                                                        
                                
                                String tmpX = parsLine[2].substring(0, 1);
                                String tmpY = parsLine[2].substring(1, 2);
                                int x = Integer.parseInt(tmpX);
                                int y = Integer.parseInt(tmpY);
                                int r = Integer.parseInt(parsLine[3]);
                                int g = Integer.parseInt(parsLine[4]);
                                int b = Integer.parseInt(parsLine[5]);
                                
                                Color otherColor = getCarpetColor(r, g, b);

                                JLabel otherCarpet = new JLabel();
                                carpet = new JLabel();
                                otherCarpet.setBackground(otherColor);
                                otherCarpet.setVisible(true);
                                otherCarpet.setOpaque(true);
                                otherCarpet.setName(Integer.toString(turn + 1));
                                carpetSquare[x][y].add(otherCarpet);                                

                                repaint();
                                revalidate();                                
                            }                            
                        }
                        else if(parsLine[0].equalsIgnoreCase("SETCARPETINGONTOP")){
                            carpetingOnTop(parsLine[1], parsLine[2], parsLine[3], parsLine[4], parsLine[5]);
                        }
                        else if(parsLine[0].equalsIgnoreCase("SENDCARPETINGONTOP")){
                            if(myTurn == false){                                
                                player[Integer.parseInt(parsLine[1])].setCarpet();
                                
                                String tmpX = parsLine[2].substring(0, 1);
                                String tmpY = parsLine[2].substring(1, 2);
                                int x = Integer.parseInt(tmpX);
                                int y = Integer.parseInt(tmpY);
                                int r = Integer.parseInt(parsLine[3]);
                                int g = Integer.parseInt(parsLine[4]);
                                int b = Integer.parseInt(parsLine[5]);
                                                                
                                carpetSquare[x][y].remove(carpetSquare[x][y].getComponent(1));
                                
                                Color otherColor = getCarpetColor(r, g, b);

                                JLabel otherCarpet = new JLabel();
                                carpet = new JLabel();
                                otherCarpet.setBackground(otherColor);
                                otherCarpet.setVisible(true);
                                otherCarpet.setOpaque(true);
                                otherCarpet.setName(Integer.toString(turn + 1));
                                carpetSquare[x][y].add(otherCarpet);                                

                                repaint();
                                revalidate();
                            }
                        }
                        else if(parsLine[0].equalsIgnoreCase("SETCANCELCARPETING")){
                            cancelCarpet(parsLine[1], parsLine[2]);
                        }
                        else if(parsLine[0].equalsIgnoreCase("SENDCANCELCARPETING")){
                            if(myTurn == false){
                                String tmpX = parsLine[2].substring(0, 1);
                                String tmpY = parsLine[2].substring(1, 2);
                                int x = Integer.parseInt(tmpX);
                                int y = Integer.parseInt(tmpY);

                                carpetSquare[x][y].remove(carpetSquare[x][y].getComponent(1));                                
                                player[Integer.parseInt(parsLine[1])].canceledCarpet();

                                revalidate();
                                repaint();
                            }
                        }                        
                        else if(parsLine[0].equalsIgnoreCase("SETCANCELCARPETINGONTOP")){
                            cancelCarpetOnTop(parsLine[1],parsLine[2],parsLine[3],parsLine[4],parsLine[5],parsLine[6],parsLine[7],parsLine[8]);
                        }
                        else if(parsLine[0].equalsIgnoreCase("SENDCANCELCARPETINGONTOP")){                            
                            if(myTurn == false){
                                String tmpX = parsLine[2].substring(0, 1);
                                String tmpY = parsLine[2].substring(1, 2);
                                int x = Integer.parseInt(tmpX);
                                int y = Integer.parseInt(tmpY);
                                int nx = Integer.parseInt(parsLine[3]);
                                int ny = Integer.parseInt(parsLine[4]);
                                int r = Integer.parseInt(parsLine[6]);
                                int g = Integer.parseInt(parsLine[7]);
                                int b = Integer.parseInt(parsLine[8]);
                                JLabel removedCarpet = new JLabel();
                                Color removedCarpetColor = getCarpetColor(r, g, b);

                                carpetSquare[x][y].remove(carpetSquare[x][y].getComponent(1));
                                removedCarpet.setName(parsLine[5]);
                                removedCarpet.setBackground(removedCarpetColor); 
                                
                                carpetSquare[nx][ny].add(removedCarpet);
                                removedCarpet.setVisible(true);
                                removedCarpet.setOpaque(true);
                                player[Integer.parseInt(parsLine[1])].canceledCarpet();

                                repaint();
                                revalidate();
                            }
                        }
                        else if(parsLine[0].equalsIgnoreCase("SETTHROWING")){
                            throwing(parsLine[1]);
                        }
                        else if(parsLine[0].equalsIgnoreCase("SENDTHROWING")){
                            if(myTurn == false){
                                int otherStep = Integer.parseInt(parsLine[1]);                                
                                moving(otherStep);                                
                            }
                        }
                        // ?????? ???????????? ????????? ?????? ????????? ????????? ?????? ??????!
                    }
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // ?????????, ?????? ????????? ????????? ?????????

    public void startGame(){
        for(int i = 0; i < playerNum; i++){
            player[i] = new Player(playerNum, true);
            if(playerNum == 2){
                player[i].setColor(i);
                player[i].setColor(i + 2);
                stateLabel[i].setText("?????????: " + player[i].getTotalDirham() + ", "
                        + "?????????: " + player[i].getCarpet() + " " 
                        + "(" + colorNames[player[i].getColor().get(0)]
                        + ", " + colorNames[player[i].getColor().get(1)] + ")");
            }
            else{
                player[i].setColor(i);
                stateLabel[i].setText("?????????: " + player[i].getTotalDirham() + ", "
                            + "?????????: " + player[i].getCarpet() + " " 
                            + "(" + colorNames[player[i].getColor().get(0)] + ")");
            }
        }
        logText.setText("????????? ???????????????!\n");        
        logText.append((turn + 1) + "?????? ???????????????." + '\n');
        logText.append("????????? ???????????????." + '\n');
        if(isOnline == false){
            setDirectionArrowVisible();
        }        
        marker.setVisible(true);
        new ControlerWindow();
        start();
    }

    public Player nowPlaying(int turn){
        return player[turn];
    }

    public void passTurn(){
        carpetCount = carpetCount + 2;
    }

    // ?????? ?????? ??????
    public void setDirectionArrowVisible(){
        Up.setVisible(true);
        Down.setVisible(true);
        Left.setVisible(true);
        Right.setVisible(true);
    }

    public void setDirectionArrowUnvisible(){
        Up.setVisible(false);
        Down.setVisible(false);
        Left.setVisible(false);
        Right.setVisible(false);
    }

    // ?????????????????? ???????????? ??????????????? ??????
    public boolean isCarpetLeft(){
        int totalCarpet = 0;
        for(int i = 0; i < playerNum; i++){
            if(player[i].getIsAlive()){
                totalCarpet += player[i].getCarpet();
            }
        }
        // System.out.println("?????? ??????" + totalCarpet);
        if(totalCarpet == 0){
            return false;
        }
        return true;
    }

    public Color getCarpetColor(int r, int g, int b){
        Color color = new Color(0);
        if(r + g + b != 255){
            color = Color.ORANGE;
        }
        else{
            if(r == 255){
                color = Color.RED;
            }
            else if(g == 255){
                color = Color.GREEN;
            }
            else if(b == 255){
                color = Color.BLUE;
            }
        }
        return color;
    }

    // ????????? ????????? ????????? ???
    public void steppedOnCarpet(int ownerNumber){
        if(isCpu == true){
            if(ownerNumber == 2){
                logText.append("CPU??? ???????????? ???????????????!" + '\n');    
            }
            else{
                logText.append("CPU??? ??????????????? ???????????? ???????????????!" + '\n');    
            }
        }
        else{
            logText.append((turn + 1) + "??? ??????????????? " + ownerNumber + "??? ??????????????? ???????????? ???????????????!" + '\n');
        }
        logText.setCaretPosition(logText.getDocument().getLength());
        
        int charge = computeCarpetPoint(ownerNumber);

        if(isCpu == true){
            if(ownerNumber == 2){
                logText.append("??????????????? " + "CPU?????? " + 
                        charge + "?????? ????????? ?????????." + '\n');
            }
            else{
                logText.append("CPU??? " + "?????????????????? " + 
                        charge + "?????? ????????? ?????????." + '\n');
            }
        }
        if(player[ownerNumber - 1].getIsAlive() == true){
            logText.append((turn + 1) + "??? ??????????????? " + ownerNumber + "??? ?????????????????? " + 
                        charge + "?????? ????????? ?????????." + '\n');
            charging(charge, ownerNumber);
        }
        else{
            logText.append(ownerNumber + "??? ??????????????? ???????????? ????????? ?????? ????????????." + '\n');
        }
                
        if(!checkSurvivor()){
            for(int i = 0; i < playerNum; i++){
                if(isCpu == true && player[1].getIsAlive() == false){
                    stateLabel[1].setText("CPU ??????");
                }
                if(player[i].getIsAlive() == false){
                    stateLabel[i].setText("Player" + (i + 1) + " ??????");
                }
            }
            isRunning = false;
        }
        else{
            if(nowPlaying(turn).getIsAlive() == true){
                if(isOnline == false && isCpu == false || myTurn == true){
                    possibleZone(checker.getRow(), checker.getColumn());
                    logText.append("???????????? ??????????????????." + '\n');
                    logText.setCaretPosition(logText.getDocument().getLength());
                }
                else if(isCpu == true && turn == 0){
                    possibleZone(checker.getRow(), checker.getColumn());
                    logText.append("???????????? ??????????????????." + '\n');
                    logText.setCaretPosition(logText.getDocument().getLength());
                }                
                else if(isCpu == true && turn == 1){
                    possibleZone(checker.getRow(), checker.getColumn());
                    setCpuCarpeting();
                }                
            }
            else{
                passTurn();
            }
        }
        logText.setCaretPosition(logText.getDocument().getLength());
    }

    // ????????? ????????? ????????? ???, ????????? ?????? ??????
    public int computeCarpetPoint(int ownerNumber){
        int[] dRow = {0, 0, 1, -1};
        int[] dColumn = {1, -1, 0, 0};

        Queue<Point> q = new LinkedList<>();
        boolean[][] visited = new boolean[7][7];
        int carpetPoint = 0;

        Point now = new Point(checker.getRow(), checker.getColumn());
        String carpetOwner = Integer.toString(ownerNumber);

        q.add(now);
        visited[checker.getRow()][checker.getColumn()] = true;
        carpetPoint++;

        while(!q.isEmpty()){
            now = q.poll();

            for(int i = 0; i < 4; i++){
                int nRow = now.x + dRow[i];
                int nColumn = now.y + dColumn[i];
                // ????????? ??? ??????, ?????? ????????? ????????? ????????????, ????????? ????????? carpetOwner??? ?????? ???
                if(0 <= nRow && nRow <= 6 && 0 <= nColumn && nColumn <= 6){
                    if(visited[nRow][nColumn] == false && carpetSquare[nRow][nColumn].getComponentCount() == 2 && 
                    carpetSquare[nRow][nColumn].getComponent(1).getName().equals(carpetOwner)){
                        visited[nRow][nColumn] = true;
                        q.add(new Point(nRow, nColumn));
                        carpetPoint++;
                    }
                }
            }
        }
        return carpetPoint;
    }
    
    public void setDirhamForChange(int charge, int owner){
        int renewedDirham = 0;

        for(int i = 0; i < playerNum; i++){
            // ?????? ????????? ?????? ????????? ?????????
            if(i == owner){
                renewedDirham = player[i].getTotalDirham() + charge;
            }
            // ????????? ???????????? ????????? ?????? ????????? ?????????
            else if(i == turn){
                renewedDirham = nowPlaying(turn).getTotalDirham() - charge;
            }
            // ????????? ?????? ?????? ????????? ?????? ????????? ?????????
            else{
                renewedDirham = player[i].getTotalDirham();
            }

            // ????????? ???????????? ?????? ?????? ?????? ?????? ????????? ??????
            player[i].resetDirmham();
            
            // 5????????? ?????????
            for(int j = 0; j < renewedDirham / 5; j++){
                player[i].addDirham5();
            }
            // 1????????? ?????????
            for(int k = 0; k < renewedDirham % 5; k++){
                player[i].addDirham1();
            }
        }
    }

    public void charging(int charge, int ownerNumber){
        int owner = ownerNumber - 1;
        // ?????? ????????? 1?????? ?????? ???????????? ?????? ????????????.
        // 5???????????? ?????? ???????????? ??????????????????.
        if(player[owner].getIsAlive() == true){
            // 1. ?????? charge?????? ????????? charge??? ?????? ???
            if(nowPlaying(turn).getTotalDirham() >= charge){
                // 1-1. charge??? 5??? ?????? ???
                if(charge % 5 == 0){
                    // 5?????? ?????????:
                    if(nowPlaying(turn).getDirham5() < 1){
                        // 1????????? ??? ??????
                        for(int i = 0; i < charge; i++){
                            nowPlaying(turn).minusDirham1();
                            player[owner].addDirham1();                                                        
                        }
                    }
                    // 5?????? ?????????:
                    else{
                        // 5?????? charge?????? ????????? ?????? ??? -> ?????? ??????
                        if(charge / 5 <= nowPlaying(turn).getDirham5()){
                            for(int i = 0; i < charge / 5; i++){
                                nowPlaying(turn).minusDirham5();
                                player[owner].addDirham5();
                            }
                        }
                        // 5?????? charge?????? ????????? ?????? ??? -> 5??? ??? ??????, ?????? ??? ?????? 1????????? ??????
                        else{
                            for(int i = 0; i < nowPlaying(turn).getDirham5(); i++){
                                nowPlaying(turn).minusDirham5();
                                player[owner].addDirham5();
                                charge = charge - 5;
                            }
                            for(int i = 0; i < charge; i++){
                                nowPlaying(turn).minusDirham1();
                                player[owner].addDirham1();
                            }
                        }
                    }
                }
                // 1-2. charge??? 5??? ????????? ?????? ???
                else{
                    // 2-1. 5????????? ?????? ??????
                    if(charge < 5){
                        // ???????????? 5?????? ?????? ??????
                        if(nowPlaying(turn).getDirham5() < 1){
                            // 1????????? ??? ??????
                            for(int i = 0; i < charge; i++){
                                nowPlaying(turn).minusDirham1();
                                player[owner].addDirham1();
                            }
                        }
                        // ???????????? 5?????? ?????? ??????
                        else{
                            // ????????? ??????????????? ????????? ???????????? ??????
                            if(player[owner].getDirham1() < 5 - charge){
                                setDirhamForChange(charge, owner);
                            }
                            // ????????? ??????????????? ?????????
                            else{
                                // 5????????? ?????? ???????????? ??????
                                nowPlaying(turn).minusDirham5();
                                player[owner].addDirham5();

                                for(int i = 0; i < 5 - charge; i++){
                                    player[owner].minusDirham1();
                                    nowPlaying(turn).addDirham1();
                                }
                            }
                        }
                    }
                    // 2-2 5????????? ??? ??????
                    else{
                        // ???????????? 5?????? ?????? ??????
                        if(nowPlaying(turn).getDirham5() < 1){
                            // 1????????? ??? ??????
                            for(int i = 0; i < charge; i++){
                                nowPlaying(turn).minusDirham1();
                                player[owner].addDirham1();
                            }
                        }
                        // ???????????? 5?????? ?????? ??????
                        else{
                            // ???????????? 5?????? ????????? 5??????????????? ?????? ?????? ??????
                            if(nowPlaying(turn).getDirham5() > charge / 5){
                                // ????????? ??????????????? ????????? ???????????? ??????
                                if(player[owner].getDirham1() < 5 - (charge % 5)){
                                    setDirhamForChange(charge, owner);
                                }
                                // ????????? ??????????????? ????????? ????????? ?????? ???????????? ??????
                                else{
                                    for(int i = 0; i < charge / 5 + 1; i++){
                                        nowPlaying(turn).minusDirham5();
                                        player[owner].addDirham5();
                                    }
                                    for(int i = 0; i < 5 - (charge % 5); i++){
                                        player[owner].minusDirham1();
                                        nowPlaying(turn).addDirham1();
                                    }
                                }
                            }
                            // ???????????? 5??? ???????????? 5?????? ?????? ??????
                            else{
                                // 5??? ?????? ?????? ?????? 1????????? ??????
                                for(int i = 0; i < nowPlaying(turn).getDirham5(); i++){
                                    nowPlaying(turn).minusDirham5();
                                    player[owner].addDirham5();
                                    charge = charge - 5;
                                }
                                for(int i = 0; i < charge; i++){
                                    nowPlaying(turn).minusDirham1();
                                    player[owner].addDirham1();
                                }
                            }
                        }
                    }
                }
            }
            

            // 2. ?????? charge?????? ?????? ???
            else if(nowPlaying(turn).getTotalDirham() < charge){
                for(int i = 0; i < nowPlaying(turn).getDirham5(); i++){
                    nowPlaying(turn).minusDirham5();
                    player[owner].addDirham5();
                }
                for(int i = 0; i < nowPlaying(turn).getDirham1(); i++){
                    nowPlaying(turn).minusDirham1();
                    player[owner].addDirham1();
                }

                nowPlaying(turn).setDead();
                for(int i = 0; i < nowPlaying(turn).getCarpet(); i++){
                    nowPlaying(turn).setCarpet();
                }
                nowPlaying(turn).resetDirmham();
                logText.append((turn + 1) + "??? ??????????????? ??????????????????." + '\n');
            }
        }
            
        // ????????? ?????? ?????? 1?????????, 5???????????? ??? ???????????? ???????????? ????????????.
        // ????????? setDead
    }

    // ?????? ??????
    public void computePlayerCarpetArea(){
        red = 0;
        orange = 0;
        blue = 0;
        green = 0;

        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                if(carpetSquare[i][j].getComponentCount() >= 2){
                    if(carpetSquare[i][j].getComponent(1).getBackground() == Color.RED ||
                    carpetSquare[i][j].getComponentCount() == 3 && carpetSquare[i][j].getComponent(2).getBackground() == Color.RED){
                        red++;
                    }
                    else if(carpetSquare[i][j].getComponent(1).getBackground() == Color.ORANGE ||
                    carpetSquare[i][j].getComponentCount() == 3 && carpetSquare[i][j].getComponent(2).getBackground() == Color.ORANGE){
                        orange++;
                    }
                    else if(carpetSquare[i][j].getComponent(1).getBackground() == Color.BLUE || 
                    carpetSquare[i][j].getComponentCount() == 3 && carpetSquare[i][j].getComponent(2).getBackground() == Color.BLUE){
                        blue++;
                    }
                    else if(carpetSquare[i][j].getComponent(1).getBackground() == Color.GREEN ||
                    carpetSquare[i][j].getComponentCount() == 3 && carpetSquare[i][j].getComponent(2).getBackground() == Color.GREEN){
                        green++;
                    }
                }
            }
        }
        carpetColorNames.add(red);
        carpetColorNames.add(orange);
        carpetColorNames.add(blue);
        carpetColorNames.add(green);
    }

    public boolean checkDraw(int winner, int winnerPoint){
        for(int i = 0; i < playerNum; i++){
            if(player[i].getIsAlive() && i != winner && player[i].getResultPoint() == winnerPoint){
                return true;
            }
        }
        return false;
    }
    public boolean checkDirhamDraw(int winner, int winnerPoint, int dirhamWinnerPoint){
        for(int i = 0; i < playerNum; i++){
            if(player[i].getIsAlive() && player[i].getResultPoint() == winnerPoint){
                if(i != winner && player[i].getTotalDirham() == dirhamWinnerPoint){
                    drawPlayers.add(i + 1);
                }
            }
        }

        if(drawPlayers.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean checkSurvivor(){
        int survivorNum = 0;
        int survivor = 0;
        for(int i = 0; i < playerNum; i++){
            if(player[i].getIsAlive() == true){
                survivorNum++;
                survivor = i;
            }
        }
        if(survivorNum == 1){
            gameResult2(survivor);
            return false;
        }
        else{
            return true;
        }
    }

    public void computeGameResult(){
        computePlayerCarpetArea();
        for(int i = 0; i < 4; i++){
            System.out.println(carpetColorNames.get(i));
        }
        int winner = 0;
        int winnerPoint = 0;
        if(playerNum == 2){
            player[0].setResultPoint(red + blue);
            player[1].setResultPoint(orange + green);
        }
        else{
            for(int i = 0; i < playerNum; i++){
                player[i].setResultPoint(carpetColorNames.get(player[i].getColor().get(0)));
            }
        }
        for(int i = 0; i < playerNum; i++){
            if(player[i].getIsAlive() && player[i].getResultPoint() >= winnerPoint){
                winnerPoint = player[i].getResultPoint();
                winner = i;
            }
        }

        if(checkDraw(winner, winnerPoint)){
            logText.append("????????? ?????????????????? ????????? ?????? ??????????????? ????????? ????????????." + '\n');
            int dirhamWinnerPoint = 0;
            for(int i = 0; i < playerNum; i++){
                if(player[i].getIsAlive() && player[i].getResultPoint() == winnerPoint){
                    if(player[i].getTotalDirham() >= dirhamWinnerPoint){
                        dirhamWinnerPoint = player[i].getTotalDirham();
                        winner = i;
                    }
                }
            }
            if(checkDirhamDraw(winner, winnerPoint, dirhamWinnerPoint)){
                for(int win : drawPlayers){
                    logText.append(win + "??? ??????????????? ????????? ?????? " + player[win].getTotalDirham() + "???" + '\n');
                }
                logText.append((winner + 1) + "??? ??????????????? ????????? ?????? " + player[winner].getTotalDirham() + "???" + '\n');
                logText.append("?????? ??????????????????!" + '\n');
                isRunning = false;
            }
            else{
                if(isCpu == true && winner == 1){
                    logText.append("CPU??? " + "????????? ?????? " +
                    dirhamWinnerPoint + "????????? ??????????????????." + '\n');
                }
                else{
                    logText.append((winner + 1) + "??? ??????????????? " + "????????? ?????? " +
                    dirhamWinnerPoint + "????????? ??????????????????!" + '\n');
                }
                isRunning = false;
            }
        }
        else{
            if(isCpu == true && winner == 1){
                logText.append("CPU??? " + "????????? ?????? " +
                winnerPoint + "????????? ??????????????????." + '\n');
            }
            else{
                logText.append((winner + 1) + "??? ??????????????? " +
                winnerPoint + "????????? ??????????????????!" + '\n');
            }
            isRunning = false;
        }        
        logText.setCaretPosition(logText.getDocument().getLength());
    }    
    

    public void gameResult2(int winner){
        if(isCpu == true){
            if(winner == 1){
                logText.append("???????????? CPU??? ??????????????????." + '\n');    
            }
            else{
                logText.append("CPU??? ???????????? ??????????????? ??????????????????!" + '\n');    
            }
        }
        logText.append("?????? ???????????? " + (winner + 1) + "??? ??????????????? ??????????????????!" + '\n');
        logText.setCaretPosition(logText.getDocument().getLength());
    }

    // cpu ???????????? ????????? ??????
    public void isAbleCarpeting(){        
        int unvisibleCount = 0;

        for(int i = 0; i < carpetZone.length; i++){
            if(!carpetZone[i].isVisible()){
                unvisibleCount++;
            }
        }
                
        if(carpetCount == 1 && unvisibleCount == carpetZone.length){
            JOptionPane.showMessageDialog(null,
            "???????????? ????????? ??? ?????? ?????? ???????????? ?????? ??????????????????.",
            "Message", JOptionPane.ERROR_MESSAGE);
            carpetCount++;
        }
    }

    public synchronized void playWithCpu(){
        PlayingWithCpu playingCpu = new PlayingWithCpu();
        playingCpu.start();
    }

    class PlayingWithCpu extends Thread{
        public PlayingWithCpu(){

        }
        public synchronized void run(){
            try{
                Thread.sleep(1000);
                setCpuDirection();
            }
            catch(InterruptedException e){
                return;
            }
            
            try{
                Thread.sleep(1000);
                throwingCpuDice();
            }
            catch(InterruptedException e){
                return;
            }                    
        }
    }

    public void setCpuDirection(){
        List<Integer> directions = new ArrayList<Integer>();
        int left = 0;
        int right = 0;
        int up = 0;
        int down = 0;
        int row = checker.getRow();
        int col = checker.getColumn();        
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                if(carpetSquare[i][j].getComponentCount() >= 2 && !(carpetSquare[i][j].getComponent(1).getBackground() == Color.LIGHT_GRAY)){
                    if(carpetSquare[i][j].getComponent(1).getName().equals(Integer.toString(1)) ||
                    carpetSquare[i][j].getComponentCount() == 3 && carpetSquare[i][j].getComponent(2).getName().equals(Integer.toString(1))){
                        if(col <= j){
                            right--;
                        }
                        if(col >= j){
                            left--;
                        }
                        if(row <= i){
                            down--;
                        }
                        if(row >= i){
                            up--;
                        }
                    }
                    else if(carpetSquare[i][j].getComponent(1).getName().equals(Integer.toString(2)) ||
                    carpetSquare[i][j].getComponentCount() == 3 && carpetSquare[i][j].getComponent(2).getName().equals(Integer.toString(2))){
                        if(col <= j){
                            right++;
                        }
                        if(col >= j){
                            left++;
                        }
                        if(row <= i){
                            down++;
                        }
                        if(row >= i){
                            up++;
                        }
                    }
                }
            }
        }        
        directions.add(up);
        directions.add(down);
        directions.add(left);
        directions.add(right);

        int max_val = -999;
        int duplicated = 0;
        int max_index = -1;

        for(int i = 0; i < directions.size(); i++){
            if(max_val <= directions.get(i)){
                if(max_val == directions.get(i)){
                    duplicated++;
                }
                max_val = directions.get(i);
                max_index = i;
            }
        }        
        if(duplicated >= 2){
            String[] positions = {"north", "south", "west", "east"};
            int randomNum = (int)(Math.random() * 4);
            checker.setDirection(positions[randomNum]);            
        }
        else{
            if(max_index == 0){
                checker.setDirection("north");
            }
            else if(max_index == 1){
                checker.setDirection("south");
            }
            else if(max_index == 2){
                checker.setDirection("west");
            }
            else if(max_index == 3){
                checker.setDirection("east");
            }
        }
        marker.setIcon(checker.getChecker(checker.getDirection()));
        System.out.println("setting");
    }

    public void throwingCpuDice(){
        moving(throwingDice());
    }

    public synchronized void setCpuCarpeting(){
        cpuCarpeting setCpu = new cpuCarpeting();
        setCpu.start();
    }

    class cpuCarpeting extends Thread{
        public cpuCarpeting(){

        }
        public void run(){
            while(carpetCount < 2){
                int visibleCount = 0;
                try{
                    Thread.sleep(2000);
                    for(int i = 0; i < carpetZone.length; i++){
                        if(carpetZone[i].isVisible()){
                            visibleCount++;
                        }
                    }
                    if(visibleCount == 0){
                        carpetCount++;
                    }
                    else{
                        setCpuCarpet();
                    }
                }
                catch(InterruptedException e){
                    return;
                }
            }
        }
    }

    public void setCpuCarpet(){
        int x;
        int y;
        int[] dx = {0,0,1,-1,0,0,2,-2};
        int[] dy = {1,-1,0,0,2,-2,0,0};
        int nx;
        int ny;
        int maxIndex = 0;
        int randomIndex;        
        String position;
        String tempX;
        String tempY;        
        Point cpuCarpetPosition = new Point();
        JLabel cpuCarpet = new JLabel();      
        int maxPoint = 0;
        int[] points = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        List<Point> positionArr = new ArrayList<Point>();

        if(nowPlaying(turn).getCarpet() >= 13){
            cpuCarpet.setBackground(color[nowPlaying(turn).getColor().get(0)]);
        }
        else{
            cpuCarpet.setBackground(color[nowPlaying(turn).getColor().get(1)]);
        }
        cpuCarpet.setOpaque(true);
        cpuCarpet.setName("2");

        for(int i = 0; i < carpetZone.length; i++){
            if(carpetZone[i].isVisible()){
                position = carpetZone[i].getParent().getName();
                tempX = position.substring(0, 1);
                tempY = position.substring(1, 2);
                x = Integer.parseInt(tempX);
                y = Integer.parseInt(tempY);
                Point temp = new Point(x, y);
                positionArr.add(temp);
                
                for(int j = 0; j < dx.length; j++){
                    nx = x + dx[j];
                    ny = y + dy[j];
                    if(0 <= nx && nx < 7 &&
                        0 <= ny && ny < 7){
                            if(carpetSquare[nx][ny].getComponentCount() >= 2){
                                if(carpetSquare[nx][ny].getComponent(1).getName() == "2" ||
                                carpetSquare[nx][ny].getComponentCount() == 3 && carpetSquare[nx][ny].getComponent(2).getName() == "2"){
                                    points[i]++;
                                }
                            }
                        }
                }
            }
        }
        
        int index = 0;
        int reps = 0;
        while(reps < points.length){
            if(points[index] == 0){
                if(index != 0){
                    index--;
                }
            }
            else{
                if(maxPoint <= points[index]){
                    maxPoint = points[index];
                    maxIndex = index;
                    index++;
                }
            }
            reps++;
        }
        
        if(maxIndex == 0 && points[0] == 0){        
            randomIndex = (int)(Math.random() * positionArr.size());
            cpuCarpetPosition = positionArr.get(randomIndex);
        }
        else{
            cpuCarpetPosition = positionArr.get(maxIndex);
        }
        
        if(carpetSquare[cpuCarpetPosition.x][cpuCarpetPosition.y].getComponentCount() == 3){
            carpetSquare[cpuCarpetPosition.x][cpuCarpetPosition.y].remove(carpetSquare[cpuCarpetPosition.x][cpuCarpetPosition.y].getComponent(2));
            carpetSquare[cpuCarpetPosition.x][cpuCarpetPosition.y].add(cpuCarpet);
        }
        else{                        
            carpetSquare[cpuCarpetPosition.x][cpuCarpetPosition.y].add(cpuCarpet);
        }
        carpetCount++;
        nowPlaying(turn).setCarpet();        
        if(carpetCount == 1){
            String sx = Integer.toString(cpuCarpetPosition.x);
            String sy = Integer.toString(cpuCarpetPosition.y);
            firstCarpetOwner = sx + sy;
        }
        if(carpetCount == 2){
            setCarpetZoneUnvisible();
        }
        repaint();
        revalidate();        
    }    
}
