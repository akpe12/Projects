// 게임 화면

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameWindow extends JFrame{
    private ImageIcon icon = new ImageIcon("/Users/chanwoo/Desktop/java/Marrakech/Images/board.png");
    private JMenuBar mb;
    private JMenu game;
    private JMenuItem gameStart;
    private JMenuItem dice;
    private JPanel mainPane;
    private JPanel gridPane;
    private JPanel infoPane;
    private JPanel infoPartPane;
    private JPanel infoPane1;
    private BoardPane boardPane;
    private JLabel infoPane2;
    private JPanel player1;
    private JPanel player2;
    private JPanel player3;
    private JPanel player4;
    private JPanel infoPane3;
    private JLabel stateLabel1;
    private JLabel stateLabel2;
    private JLabel stateLabel3;
    private JLabel stateLabel4;
    private JTextArea logText;
    // public static squareGrid[][] square = new squareGrid[7][7];
    private JPanel[][] square = new JPanel[7][7];
    
    public GameWindow(){
        setTitle("Marrakech");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new GridLayout(1, 2));

        createMenuBar();
        createBoard();
        createInfo();

        setSize(800, 500);
        //창 크기 조절 제한
        setResizable(false);
        setVisible(true);
    }
    public void createMenuBar(){
        //게임시작 메뉴 만들기
        mb = new JMenuBar();

        game = new JMenu("게임");
        gameStart = new JMenuItem("게임시작");
        dice = new JMenuItem("주사위 던지기");

        gameStart.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // 게임 시작 버튼 누르면 나오게끔
                // 작동하는지 확인 위해 임시로 해놓은 것
                stateLabel1.setText("양탄자, 디르함");
                stateLabel2.setText("양탄자, 디르함");
                stateLabel3.setText("양탄자, 디르함");
                stateLabel4.setText("양탄자, 디르함");

                logText.setText("게임을 시작합니다!\n");
                marker.setVisible(true);
                
            }
        });

        dice.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new ControlerWindow();
            }
        });

        game.add(gameStart);
        game.add(dice);
        mb.add(game);

        setJMenuBar(mb);
    }
    
    public void createBoard(){
        //메인 패널 생성(모든 보드에 대한 내용이 담길 기본의 맨 밑의 팬)
        mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());
        mainPane.setBorder(new EmptyBorder(50, 30, 50, 20));

        //보드 이미지를 생성해주는 팬 생성
        boardPane = new BoardPane();
        boardPane.setLayout(new BorderLayout());
        boardPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        //7X7 격자 보드판을 그릴 밑바닥이 되는 gridPane 생성
        gridPane = new JPanel();
        gridPane.setLayout(new GridLayout(7, 7));
        gridPane.setOpaque(false);


        for(int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                square[i][j] = new JPanel();
                square[i][j].setLayout(new BorderLayout());
                square[i][j].setOpaque(false);
                // square[i][j].setBackground((i + j) % 2 == 0 ? Color.BLACK : Color.LIGHT_GRAY);
                // boardPane.add(square[i][j]);
                gridPane.add(square[i][j]);
            }
        }
        //7X7 보드판 패널을 이미지 패널에 붙이기
        boardPane.add(gridPane);

        //7X7 보드판 패널과 보드 이미지 패널을 mainPane에 붙이기
        mainPane.add(boardPane);
        
        add(mainPane);
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

        //플레이어의 상태, 게임 로그 패널이 들어있는 패널
        infoPartPane = new JPanel();
        infoPartPane.setLayout(new GridLayout(3, 1, 0, 15));

        //1에 p1, p2들어간다
        infoPane1 = new JPanel();
        infoPane1.setLayout(new GridLayout(2, 1, 0, 15));

        //player1의 패널
        player1 = new JPanel();
        player1.setLayout(new BorderLayout());

        player1.setBackground(Color.LIGHT_GRAY);

        stateLabel1 = new JLabel("Player1");
        stateLabel1.setFont(new Font("Arial", Font.BOLD, 20));
        stateLabel1.setHorizontalAlignment(JLabel.CENTER);
        player1.add(stateLabel1);
        
        infoPane1.add(player1);

        //player2
        player2 = new JPanel();
        player2.setLayout(new BorderLayout());

        player2.setBackground(Color.LIGHT_GRAY);
        stateLabel2 = new JLabel("Player2");
        stateLabel2.setFont(new Font("Arial", Font.BOLD, 20));
        stateLabel2.setHorizontalAlignment(JLabel.CENTER);

        player2.add(stateLabel2);
        infoPane1.add(player2);

        infoPane2 = new JLabel();
        infoPane2.setLayout(new GridLayout(2, 1, 0, 15));

        //player3
        player3 = new JPanel();
        player3.setLayout(new BorderLayout());
        player3.setBackground(Color.LIGHT_GRAY);

        stateLabel3 = new JLabel("Player3");
        stateLabel3.setFont(new Font("Arial", Font.BOLD, 20));
        stateLabel3.setHorizontalAlignment(JLabel.CENTER);
        
        player3.add(stateLabel3);

        infoPane2.add(player3);

        //player4
        player4 = new JPanel();
        player4.setLayout(new BorderLayout());
        player4.setBackground(Color.LIGHT_GRAY);

        stateLabel4 = new JLabel("Player4");
        stateLabel4.setFont(new Font("Arial", Font.BOLD, 20));
        stateLabel4.setHorizontalAlignment(JLabel.CENTER);

        player4.add(stateLabel4);

        infoPane2.add(player4);

        //log 칸
        infoPane3 = new JPanel();
        //append로 area에 기록 남기기
        logText = new JTextArea("Log", 7, 29);
        
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
    // public int rollDice(){
    //     int[] dice = {1, 2, 2, 3, 3, 4};

    //     return dice[(int)(Math.random() * 6)];
    // }
    class ControlerWindow extends JFrame {
        private final int WIDTH = 300;
        private final int HEIGHT = 300;
        public ControlerWindow(){
            setTitle("Controler");
            Container c = getContentPane();
            c.setLayout(new GridLayout(1, 1));
    
            JButton throwingDice = new JButton("주사위 던지기");
            throwingDice.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    // moving(throwingDice());
                    // 주사위를 던지면
                    // 움직이도록 함
                }   
            });
    
            c.add(throwingDice);
    
            setSize(WIDTH, HEIGHT);
            setVisible(true);
        }
    }
    public static void main(String[] args){
        new GameWindow();
    }
}