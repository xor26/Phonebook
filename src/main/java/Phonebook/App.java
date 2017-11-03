package Phonebook;

import java.sql.SQLException;
import java.util.ArrayList;

import static javax.swing.SwingUtilities.updateComponentTreeUI;

public class App {
    private static MainFrame mainFrame;
    static ArrayList<Contact> contacts;
    static ArrayList<String> groups;

    public static void main(String[] args) throws SQLException {
        System.out.println("Er we go");

        mainFrame = new MainFrame();
        updateComponentTreeUI(mainFrame);
        mainFrame.setVisible(true);

        DBGateway.connect();
        contacts = DBGateway.getContactsList();
        for (Contact conn: contacts){
            mainFrame.addContactToView(conn);
        }

        //get all possible group
        groups = new DBGateway().getGroupList();

        System.out.println("Er we stop");
    }

    static void showAddNewContactFrame(){
        ContactFrame addFrame = new ContactFrame();
        addFrame.setVisible(true);
    }

    static void AddNewContact(String firstName, String secondName){
        Contact contact = new Contact();
        contact.setFirstName(firstName);
        contact.setSecondName(secondName);
        contact.create();

        contacts.add(contact);
        mainFrame.addContactToView(contact);
    }

    static void showEditContactFrame(){
        ContactFrame editFrame = new ContactFrame();
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        String oldFistName = contacts.get(selectedContactIndex).getFirstName();
        String oldSecondName = contacts.get(selectedContactIndex).getSecondName();
        editFrame.setFrameToEdit(oldFistName, oldSecondName);

        editFrame.setVisible(true);
    }

    static void editContact(String newFirstName, String newSecondName){
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        int contactId = contacts.get(selectedContactIndex).getId();

        Contact contact = DBGateway.getContactFromDB(contactId);
        contact.setFirstName(newFirstName);
        contact.setSecondName(newSecondName);
        contact.save();

        contacts.set(selectedContactIndex, contact);
        mainFrame.editContactInView(contact, selectedContactIndex);
    }


    static void removeContact(int index){
        int id = contacts.get(index).getId();

        DBGateway.deleteContact(id);
        contacts.remove(index);
    }

    static void saveGroup(String groupName, int contactIndex){
        int contactId = contacts.get(contactIndex).getId();
        DBGateway.setGroup(contactId, groupName);
        contacts.get(contactIndex).setGroup(groupName);
    }

    static void showAddPhoneFrame(){
        PhoneFrame addPhoneFrame = new PhoneFrame();
        addPhoneFrame.setVisible(true);
    }

    static void addPhone(String phone) {
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        int contactId = contacts.get(selectedContactIndex).getId();
        DBGateway.addPhone(contactId, phone);
        contacts.get(selectedContactIndex).addPhone(phone);
        mainFrame.setInfoPanel();
    }

    static void showEditPhoneFrame(String oldPhone){
        PhoneFrame editPhoneFrame = new PhoneFrame();
        editPhoneFrame.setFrameToEdit(oldPhone);
        editPhoneFrame.setVisible(true);

    }

    static void editPhone(String newPhone, String oldPhone){
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        int contactId = contacts.get(selectedContactIndex).getId();
        DBGateway.editPhone(contactId, newPhone, oldPhone);
        contacts.get(selectedContactIndex).removePhone(oldPhone);
        contacts.get(selectedContactIndex).addPhone(newPhone);
        mainFrame.setInfoPanel();
    }

    static void removePhone(String phone) {
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        int contactId = contacts.get(selectedContactIndex).getId();
        DBGateway.removePhone(contactId, phone);
        contacts.get(selectedContactIndex).removePhone(phone);
        mainFrame.setInfoPanel();
    }


}
