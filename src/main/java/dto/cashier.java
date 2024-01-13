package dto;

public class cashier {
    public Integer id;
    public String name;
    public String phoneNumber;
    public String address;

    public cashier(int id, String name, String phoneNumber, String address){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void insertCashier(String name, String phoneNumber, String address){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }
    public int getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setID(int id) {
        this.id = id;
    }
}
