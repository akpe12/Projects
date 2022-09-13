// 필드 변수들 정보 반환 가능
// 필드 변수들 정보를 변경할 수 있는 set 메소드 구현되어 있음
public class Person {
    private String ID;
    private String name;
    private String passward;

    public Person(String ID, String name, String passward){
        this.ID = ID;
        this.name = name;
        this.passward = passward;
    }

    public String getID(){
        return ID;
    }

    public String getName(){
        return name;
    }

    public String getPassward(){
        return passward;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPassward(String passward){
        this.passward = passward;
    }
}
