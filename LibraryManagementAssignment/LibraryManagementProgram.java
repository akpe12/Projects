import java.util.Scanner;

public class LibraryManagementProgram {
    private static Scanner scan = new Scanner(System.in);
    private static final String managerID = "0000";
    private static final String managerName = "홍길동";
    private static final String managerPWD = "1234";

    private static TotalLibraryManage libManager = new TotalLibraryManage();
    static Person currentPerson = null;
    public static MemberManage membermanage = new MemberManage();

    public static void main(String[] args){
        //제공되는 매니저의 정보를 담아 매니저 객체를 만든다.
        Manager manager = new Manager(managerID, managerName, managerPWD);
        //회원가입 할 회원들의 정보를 담기 위한 회원 객체를 만든다.
        Member member;
        while(true){
            printMainMenu();
            System.out.print("입력 >> ");
            int selectNum = scan.nextInt();

            //관리자 모드
            if(selectNum == 1){
                //로그인을 해야 이용 가능
                //정보가 일치하지 않으면 초기 메뉴로!
                System.out.println("- - - - - - 관리자 모드입니다. 로그인 하십시오. - - - - - - - - - - - - - - - - - - - -");
                System.out.print("ID: ");
                String id = scan.next();
                System.out.print("비밀번호: ");
                String passward = scan.next();
                //관리자 아이디와 비밀번호가 일치한다면
                if((managerID.equals(id)) && (managerPWD.equals(passward))){
                    System.out.println("로그인 되었습니다.");
                    //로그인 한 인원인 매니저 객체를 currentPerson 레퍼런스가 가리키도록 한다.
                    currentPerson = manager;
                    while(true){
                        //1. 도서 관리 2. 로그아웃(currenPerson = null 후 break;)
                        printManagerMenu2();
                        System.out.print("입력 >> ");
                        int selectMenu = scan.nextInt();
                        
                        if(selectMenu == 1){
                            // printManagerMenu();
                            libManager.ManagerRun(currentPerson);
                        }
                        else if(selectMenu == 2){
                            currentPerson = null;
                            break;
                        }
                    }
                }
                //입력한 정보가 관리자 아이디, 비밀번호와 다르다면
                else{
                    System.out.println("관리자 아이디 혹은 비밀번호가 일치하지 않아 초기 화면으로 돌아갑니다.");
                    continue;
                }
            }
            //회원 메뉴
            else if(selectNum == 2){
                while(true){
                    printMemberMenu2();
                    System.out.print("입력 >> ");
                    int selectMenu = scan.nextInt();

                    if(selectMenu == 1){
                        membermanage.signUp();
                    }
                    else if(selectMenu == 2){
                        member = membermanage.Login();
                        //로그인 한 정보를 member객체에 담고 그 객체를 currentPerson
                        //레퍼런스가 가리키도록 한다.
                        currentPerson = member;
                    }
                    else if(selectMenu == 3){
                        //로그인 하지 않은 상태에서 도서 대출 메뉴를 눌렀을 시
                        if(currentPerson == null){
                            System.out.println("로그인 해야 이용할 수 있습니다.");
                        }
                        //로그인 한 상태에서 도서 대출 메뉴를 눌렀을 시
                        else if(currentPerson != null){
                            libManager.MemberRun(currentPerson);
                        }
                    }
                    //로그아웃을 하면서 현재 로그인 한 인원을 나타내주는
                    //currentPerson이 null을 가리키도록 한다.
                    else if(selectMenu == 4){
                        currentPerson = null;
                        break;
                    }
                }
            }
            else if(selectNum == 3){
                System.out.println("프로그램을 종료합니다.");
                break;
            }
        }
    }

    public MemberManage getMemberManage(){
        //회원가입 한 회원들의 정보를 TotalLibraryManage클래스로 넘길 수 있도록
        //현 클래스 내에 있는 membermanage를 참조할 수 있는
        //메소드를 생성한다.
        return membermanage;
    }

    public static void printMainMenu(){
        System.out.println("- - - - - - 전북대학교 컴퓨터 공학과 도서관 관리 프로그램입니다. - - - - - - - - -");
        System.out.println("1. 관리자 메뉴 | 2. 회원 메뉴 | 3. 종료");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    }

    public static void printManagerMenu(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("1. 전체 도서 목록 출력 | 2. 도서 등록 | 3. 도서 재고 추가 | 4. 회원 목록 보기 | 5. 종료");
        System.out.println("-------------------------------------------------------------------------");
    }

    public static void printManagerMenu2(){
        System.out.println("- - - - - - - - - - - - - 관리자 모드 - - - - - - - - - - - - -");
        System.out.println("1. 도서 관리 | 2. 로그아웃");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    }

    public static void printMemberMenu(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("1. 전체 도서 목록 출력 | 2. 도서 대여 | 3. 도서 반납 | 4. 대여 도서 목록 | 5. 돌아가기");
        System.out.println("-------------------------------------------------------------------------");
    }

    public static void printMemberMenu2(){
        System.out.println("- - - - - - - - - - - - - 회원 모드 - - - - - - - - - - - - -");
        System.out.println("1. 회원가입 | 2. 로그인 | 3. 도서 대출 | 4. 로그아웃");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    }
}
