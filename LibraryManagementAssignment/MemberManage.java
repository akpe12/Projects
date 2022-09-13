import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class MemberManage {
    private Scanner scan;
    private HashMap<String, Member> memberHash;
    //<member id, Member>

    public MemberManage(){
        this.scan = new Scanner(System.in);
        this.memberHash = new HashMap<String, Member>();
    }

    public void signUp(){
        // 회원 ID, 이름, 비밀번호를 입력받아 memberHash에 추가
        // 루프를 돌면서 ID가 이미 존재하면 "이미 존재하는 ID입니다" 출력 후
        // 재입력 받기
        // 재입력된 ID가 중복되지 않는 ID면 루프 탈출
        // 회원가입 성공이면 회원가입 완료 띄우기
        System.out.print("ID: ");
        String id = scan.next();
        //memberHash에 이미 존재하는 id라면
        if(memberHash.containsKey(id)){
            System.out.println("이미 존재하는 ID입니다.");
            //memberHash에 없는 id를 생성할 때까지 반복적으로 묻는다.
            while(true){
                System.out.println("ID: ");
                id = scan.next();
                if(!memberHash.containsKey(id)){
                    break;
                }
            }
        }
        System.out.print("이름: ");
        String name = scan.next();
        System.out.print("비밀번호: ");
        String passward = scan.next();
        Member newMember = new Member(id, name, passward);
        //그렇게 입력한 정보들을 memberHash에 담아준다.
        memberHash.put(id, newMember);

        System.out.println("회원가입 완료");
    }

    public Member Login(){
        Member member = null;
        //ID가 존재하지 않거나 비밀번호가 틀릴 경우 메시지 출력 후 null로 반환
        //로그인 성공 시 해당 멤버 객체 반환
        System.out.print("ID: ");
        String id = scan.next();
        System.out.print("비밀번호: ");
        String passward = scan.next();

        //memberHash에 로그인 시 입력한 정보들과 일치한 회원이 있다면
        if((memberHash.containsKey(id)) && (memberHash.get(id).getPassward().equals(passward))){
            System.out.println("로그인 되었습니다.");
            return memberHash.get(id);
        }
        //memberHash에 로그인 시 입력한 정보들과 일치한 회원이 없다면
        else{
            System.out.println("존재하지 않는 아이디거나 비밀번호가 맞지 않습니다.");
            return member;
        }
    }

    public void PrintMemberList(){
        Set<String> keys = memberHash.keySet();
        Iterator<String> it = keys.iterator();

        while(it.hasNext()){
            String key = it.next();
            Member member = memberHash.get(key);

            System.out.println("ID: " + member.getID() + 
            ", 이름: " + member.getName());
        }
    }
}
