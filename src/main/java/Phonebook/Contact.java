package Phonebook;

import java.util.ArrayList;

public class Contact {
    private int id;
    private String firstName;
    private String secondName;
    private String group;
    private ArrayList<String> phones = new ArrayList<String>();

    int getId() {
        return id;
    }

    String getFirstName() {
        return firstName;
    }

    String getSecondName() {
        return secondName;
    }

    String getGroup() {
        return group;
    }

    ArrayList<String> getPhones() {return phones;}


    void setId(int id){
        this.id = id;
    }

    void setFirstName(String firstName){
        this.firstName = firstName;
    }

    void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    void setGroup(String group) {
        this.group = group;
    }

    void addPhone(String phone) {
        phones.add(phone);
    }

    void removePhone(String phone) { phones.remove(phone); }

    void create(){
        DBGateway.insertContact(this);
    }

    void save(){
        DBGateway.saveContactToDB(this);
    }
}
