// Book 클래스 마무리 후 정리

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class Member extends Person {
    HashMap<String, Book> bookhash = new HashMap<String, Book>();// 대여 중인 도서를 저장하는 hash

    public Member(String ID, String name, String passward){
        super(ID, name, passward);
    }

    HashMap<String, Book> getbookHash(){
        return bookhash;
    }

    void PrintRentalList(){
        Set<String> keys = bookhash.keySet();
        Iterator<String> it = keys.iterator();
        while(it.hasNext()){
            String key = it.next();
            Book book = bookhash.get(key);
            
            System.out.println("도서 번호 : " + book.getBookNumber() + ", 도서 명 : " + 
            book.getName() + ", 도서 장르 : " + book.getGenre());
        }
    }
}
