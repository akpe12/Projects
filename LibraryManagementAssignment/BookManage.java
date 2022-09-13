import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Set;

public class BookManage {
    private Scanner scan;
    private HashMap<String, Book> bookhash;

    public BookManage(){
        this.scan = new Scanner(System.in);
        this.bookhash = new HashMap<String, Book>();
    }

    void printBookList(){
        //bookhash에 저장된 모든 도서 정보 출력
        Set<String> keys = bookhash.keySet();
        Iterator<String> it = keys.iterator();

        while(it.hasNext()){
            String key = it.next();
            Book book = bookhash.get(key);

            System.out.println("도서 번호 : " + book.getBookNumber() + ", 도서 명 : " + 
            book.getName() + ", 도서 장르 : " + book.getGenre() + ", 재고 : " + book.getStock());
        }
    }

    boolean AddBook(){
        // 도서 등록 메뉴의 기능!
        // 현재 보유 중이지 않은 도서만 추가 가능하도록 제약!
        // -> 현재 bookhash에 같은 도서 번호의 도서가 있는지 check
        // -> 같은 도서 번호의 책이 없으면 수량 입력 받기!
        // -> 같은 도서 번호의 책이 있으면 추가 불가!
        // 도서 번호랑 추가하고자 하는 수량을 입력 받는다
        // 이때 추가하고자 하는 수량이 0이거나 0보다 작으면 추가 불가능하도록 제약!

        System.out.print("도서 번호: " );
        String bookNumber = scan.next();
        //새로 등록하고자 하는 도서가 이미 bookhash에 있다면
        if(bookhash.containsKey(bookNumber)){
            System.out.println("이미 존재하는 도서입니다.");
            return false;
        }
        //새로 등록하고자 하는 도서가 bookhash에 없다면
        else{
            System.out.print("도서 이름: " );
            String name = scan.next();
            System.out.print("도서 장르: " );
            String genre = scan.next();
            System.out.print("도서 수량: " );
            int stock = scan.nextInt();

            //등록하고자 하는 수량이 0개 이하라면
            if(stock <= 0){
                System.out.println("1개 이상부터 등록할 수 있습니다.");
                return false;
            }
            //정상적으로 도서의 정보가 들어왔다면
            else{
                //입력받은 도서의 정보로 객체를 생성한다.
                Book newBook = new Book(bookNumber, name, genre, stock);
                //등록을 위해 새로운 책의 객체를 bookhash에 넣어준다.
                bookhash.put(bookNumber, newBook);
            }
            return true;
        }
    }

    boolean UpdateBookStock(){
        System.out.println();
        System.out.print("추가하고자 하는 도서 번호: ");
        String bookNumber = scan.next();

        //수량 추가를 원하는 도서가 bookhash에 등록되어 있는 도서라면
        if(bookhash.containsKey(bookNumber)){
            System.out.print("추가하고자 하는 수량: ");
            int stock = scan.nextInt();
            //추가하고자 하는 수량이 0개 이하라면
            if(stock <= 0){
                System.out.println("1개 이상 추가해야 합니다.");
                return false;
            }
            //1개 이상 추가하고자 한다면
            else{
                System.out.println("[도서번호: " + bookhash.get(bookNumber).getBookNumber() + "] + [도서명 : " + 
                bookhash.get(bookNumber).getName() + "]의 수량이 " + bookhash.get(bookNumber).getStock() + "에서 " + 
                (bookhash.get(bookNumber).getStock() + stock) + "로 증가되었습니다.");
                //추가하고자 하는 수량을 넘겨준다.
                bookhash.get(bookNumber).updateStock(stock);
            }
        }
        //재고 추가를 위해 검색한 도서가 등록되어있지 않은 도서라면
        else{
            System.out.println("존재하지 않는 도서입니다.");
            return false;
        }
        return true;
    }

    boolean ReturnBook(Member member){
        //도서 번호를 입력받아 현재 회원이 대여 중인 도서 반납
        //회원이 대여 중인 도서만 반납 가능하도록 제한!
        //반납했으니 도서 재고를 올려주어라!

        System.out.print("도서 번호: ");
        String bookNumber = scan.next();
        //도서 번호를 검색했을 때, 회원이 대여하고 있는 도서의 도서 번호가 같다면
        if(member.getbookHash().containsKey(bookNumber)){
            //회원의 해시맵에서 해당 도서를 반납시킨다.
            member.getbookHash().get(bookNumber).SubtractStock();
            //회원의 해시맵에서 해당 도서를 삭제한다.
            member.getbookHash().remove(bookNumber);
            //bookhash에 해당 도서의 재고를 1 올린다.
            bookhash.get(bookNumber).AddStock();
            System.out.println("[도서번호: " + bookNumber + "] + [도서명: " + bookhash.get(bookNumber).getName() + 
            "]이 정상적으로 반납되었습니다.");
        }
        //회원이 대여하고 있는 도서 번호와 일치하지 않다면
        else{
            System.out.println("대여 중인 도서가 아닙니다.");
            return false;
        }
        return true;
    }

    boolean RentalBook(Member member){
        //도서 번호를 입력 받아 현재 도서 대여
        //도서가 존재하지 않거나 재고가 0개이면 대여 불가
        //-> bookhash에서 확인!!
        //대여하는 책의 수량은 1개만 가능
        //대여하면 bookhash에서의 재고는 -1
        //멤버의 bookhash에서의 재고는 +1
        System.out.print("도서 번호: ");
        String bookNumber = scan.next();
        //bookhash에 검색한 도서 번호와 같은 도서 번호가 있고 해당 도서의 재고가 1개 이상이라면
        if((bookhash.containsKey(bookNumber)) && (bookhash.get(bookNumber).getStock() > 0)){
            //bookhash에서 해당 도서의 재고를 1 뺀다.
            bookhash.get(bookNumber).SubtractStock();
            //대여할 도서의 정보를 담아 객체를 생성한다.
            Book loanBook = new Book(bookNumber, bookhash.get(bookNumber).getName(), bookhash.get(bookNumber).getGenre(), 1);
            //해당 객체를 memberhash에 저장하여 대여한다.
            member.getbookHash().put(bookNumber, loanBook);
            //member.getbookHash().put(bookNumber, loanBook);
            System.out.println("[도서번호: " + bookNumber + "] + [도서명: " + member.getbookHash().get(bookNumber).getName() + 
            "]이 정상적으로 대여되었습니다.");
        }
        //검색한 도서 번호가 bookhash에 존재하지 않는 도서 번호라면
        else{
            System.out.println("도서가 존재하지 않거나 재고가 부족하여 대여가 불가능합니다.");
            return false;
        }
        return true;
    }
}
