public class Book {
    String book_number;
    String name;
    String genre;
    int stock;

    public Book(String book_number, String name, String genre, int stock){
        this.book_number = book_number;
        this.name = name;
        this.genre = genre;
        this.stock = stock;
    }

    String getBookNumber(){
        return book_number;
    }

    String getName(){
        return name;
    }

    String getGenre(){
        return genre;
    }

    int getStock(){
        return stock;
    }

    // 원하는 수량만큼 재고 증가시키는 메소드
    void updateStock(int new_stock){
        this.stock += new_stock;
    }

    void AddStock(){
        this.stock += 1;
    }

    void SubtractStock(){
        this.stock -= 1;
    }
}
