import java.net.*;
import java.io.*;
import java.util.*;

public class ClientForMath {
    //먼저 서버에게 보낼 수식값을 입력
    //입력할 때 양식 -> "계산식(빈칸으로 띄워서 입력)"
    //서버에게 받은 계산값 화면에 출력 ""
    //출력할 때 양식 -> "계산 결과: "
    //bye입력하면 종료
    public static void main(String[] args){
        BufferedReader in = null;
        BufferedWriter out = null;
        //클라이언트 소켓 열어야 된다.
        Socket socket = null;
        //입력 받기 위한 scanner
        Scanner scan = new Scanner(System.in);

        try{
            socket = new Socket("localhost", 9999);
            System.out.println("연결되었습니다.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while(true){
                //서버에게 전달할 수식 입력 받기
                System.out.print("계산식(빈칸으로 띄어서 입력)>> ");
                String clientMsg = scan.nextLine();
                //bye를 입력했으면 강제종료
                if(clientMsg.equalsIgnoreCase("bye")){
                    //서버는 왜 client가 종료되는지 알아야 하므로 flush 해주기
                    out.write(clientMsg + "\n");
                    out.flush();
                    break;
                }
                //수식 서버에게 전달하기
                out.write(clientMsg + "\n");
                out.flush();

                //계산값을 서버에게 받기
                String result = in.readLine();
                //계산값 화면에 띄우기
                System.out.println("계산 결과: " + result);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        finally{
            try{
                scan.close();
                if(socket != null){
                    socket.close();
                }
            }
            catch(IOException e){
                System.out.println("에러로 인해 종료되었습니다.");
            }
        }
    }
}
