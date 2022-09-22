import java.io.*;
import java.net.*;
import java.util.*;

public class ServerForMath {
    public static String mathHelper(String exp){
        StringTokenizer st = new StringTokenizer(exp, " ");
        if(st.countTokens() != 3){
            return "error";
        }
        // String[] requestedExp = new String[st.countTokens()];
        // int idx = 0;
        int left = Integer.parseInt(st.nextToken());
        String opr = st.nextToken();
        int right = Integer.parseInt(st.nextToken());
        // char opr;
        // int ans = 0;
        String answer = "";

        // while(st.hasMoreTokens()){
        //     requestedExp[idx] = st.nextToken();
        //     idx++;
        // }
        // left = Integer.parseInt(requestedExp[0]);
        // opr = requestedExp[1].charAt(0);
        // right = Integer.parseInt(requestedExp[2]);

        switch(opr){
            case "+":
                answer = Integer.toString(left + right);
                break;
            case "-":
                answer = Integer.toString(left - right);
                break;
            case "*":
                answer = Integer.toString(left * right);
                break;
            case "//":
                answer = Integer.toString(left / right);
                break;
            default:
                answer = "error";
                break;
        }
        return answer;
    }
    //클라이언트에게 수식을 받기
    //받은 수식을 화면에 출력하기
    //받은 수식을 계산하기
    //계산 결과를 클라이언트에게 전송하기 flush
    //클라이언트가 bye를 입력했으면, "클라이언트에서 연결을 종료하였음" 출력
    public static void main(String[] args){
        BufferedReader in = null;
        BufferedWriter out = null;
        ServerSocket listener = null;
        //연결이 되면 새로운 객체를  생성하기 위한 socket 선언
        Socket socket = null;

        try{
            //우선 서버 연결 시도부터
            listener = new ServerSocket(9999);
            System.out.println("서버 연결 중입니다.");

            socket = listener.accept();
            System.out.println("연결되었습니다.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while(true){
                //클라이언트에게 수식을 받기
                String clientMsg = in.readLine();
                String total;
                if(clientMsg.equalsIgnoreCase("bye")){
                    System.out.println("클라이언트에서 연결을 종료하였음");
                    break;
                }
                System.out.println(clientMsg);
                //받은 수식 계산하기
                total = mathHelper(clientMsg);
                out.write(total + "\n");
                out.flush();
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        finally{
            try{
                if(socket != null){
                  socket.close();
                }
                if(listener != null){
                  listener.close();
                }
            }
            catch(IOException e){
                System.out.println("에러로 인한 연결 종료");
            }
        }
    }
}
