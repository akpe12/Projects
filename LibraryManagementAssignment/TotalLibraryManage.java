import java.util.Scanner;

public class TotalLibraryManage {
    MemberManage memberManage;
    BookManage bookManage;

    private Scanner scan;

    public TotalLibraryManage(){
        memberManage = new MemberManage();
        bookManage = new BookManage();
        scan = new Scanner(System.in);
    }

    public MemberManage getmemberManage(){
        return memberManage;
    }

    public BookManage getbookManage(){
        return bookManage;
    }

    public void printManagerMenu(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("1. 전체 도서 목록 출력 | 2. 도서 등록 | 3. 도서 재고 추가 | 4. 회원 목록 보기 | 5. 종료");
        System.out.println("-------------------------------------------------------------------------");
    }

    public void printMemberMenu(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("1. 전체 도서 목록 출력 | 2. 도서 대여 | 3. 도서 반납 | 4. 대여 도서 목록 | 5. 돌아가기");
        System.out.println("-------------------------------------------------------------------------");
    }

    // 관리자용
    public void ManagerRun(Person person){
        // loop를 돌면서 관리자 기능 수행
        // 1. 전체 도서 목록 출력
        // 2. 도서 등록
        // 3. 도서 재고 추가
        // 4. 회원 목록 보기
        // 5. 돌아가기(loop 탈출)
        while(true){
            printManagerMenu();
            System.out.print("<선택> ");
            int selectNum = scan.nextInt();

            if(selectNum == 5){
                System.out.println("프로그램을 종료합니다.");
                break;
            }
            else{
                if(selectNum == 1){
                    bookManage.printBookList();
                }
                else if(selectNum == 2){
                    bookManage.AddBook();
                }
                else if(selectNum == 3){
                    bookManage.UpdateBookStock();
                }
                else if(selectNum == 4){
                    //회원가입이 LibraryManagementProgram에 있는
                    //membermanage에서 이뤄지고 있으므로
                    LibraryManagementProgram.membermanage.PrintMemberList();
                }
            }
        }
    }

    // 회원용
    public void MemberRun(Person person){
        // loop를 돌면서 회원 기능 수행
        // 1. 전체 도서 목록 출력
        // 2. 도서 대여
        // 3. 도서 반납
        // 4. 대여 도서 목록
        // 5. 돌아가기(loop 탈출)
        Member member = (Member)person;

        while(true){
            printMemberMenu();
            System.out.print("<선택> ");
            int selectNum = scan.nextInt();

            if(selectNum == 5){
                System.out.println("이전 화면으로 돌아갑니다.");
                break;
            }
            else{
                if(selectNum == 1){
                    bookManage.printBookList();
                }
                else if(selectNum == 2){
                    bookManage.RentalBook(member);
                }
                else if(selectNum == 3){
                    bookManage.ReturnBook(member);
                }
                else if(selectNum == 4){
                    member.PrintRentalList();;
                }
            }
        }
    }
}
