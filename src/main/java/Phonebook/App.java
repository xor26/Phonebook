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

        //get all possible group variants
        groups = new DBGateway().getGroupList();

        System.out.println("Er we stop");
    }

    /**
     * Show ContactFrame with empty fields
     */
    static void showAddNewContactFrame(){
        ContactFrame addFrame = new ContactFrame();
        addFrame.setVisible(true);
    }

    /**
     * Create new contact in table contact then add it to App.contacts, and view
     * @param firstName - come from user input in ContactFrame(firstNameTextField)
     * @param secondName come from user input in ContactFrame (secondNameTextField)
     */
    static void AddNewContact(String firstName, String secondName){
        Contact contact = new Contact();
        contact.setFirstName(firstName);
        contact.setSecondName(secondName);
        contact.create();

        contacts.add(contact);
        mainFrame.addContactToView(contact);
    }

    /**
     * Show ContactFrame, and set it for editing existing contact
     * contact must be selected in MainFrame
     * info for fields come from App.contacts
     */
    static void showEditContactFrame(){
        ContactFrame editFrame = new ContactFrame();
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        String oldFistName = contacts.get(selectedContactIndex).getFirstName();
        String oldSecondName = contacts.get(selectedContactIndex).getSecondName();
        editFrame.setFrameToEdit(oldFistName, oldSecondName);

        editFrame.setVisible(true);
    }

    /**
     * Set new first and second names for contact in DB, App.contacts and view
     * call from addButton in ContactFrame
     * @param newFirstName string from ContactFrame.firstNameTextField
     * @param newSecondName string from ContactFrame.secondNameTextField
     */
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


    /**
     * Remove selected in main frame contact by index in MainFrame.contactsList
     * Indices in MainFrame.contactsList are equal to indices in App.contacts
     * @param index in MainFrame.contactsList and App.contacts
     */
    static void removeContact(int index){
        int id = contacts.get(index).getId();

        DBGateway.deleteContact(id);
        contacts.remove(index);
    }

    /**
     * Set new group for contact
     * @param groupName Value come from MainFrame.groupComboBox
     * @param contactIndex index of currently selected contact
     */
    static void saveGroup(String groupName, int contactIndex){
        int contactId = contacts.get(contactIndex).getId();
        DBGateway.setGroup(contactId, groupName);
        contacts.get(contactIndex).setGroup(groupName);
    }

    /**
     * Show PhoneFrame with empty fields
     */
    static void showAddPhoneFrame(){
        PhoneFrame addPhoneFrame = new PhoneFrame();
        addPhoneFrame.setVisible(true);
    }

    /**
     * Add new phone to contact in DB, App.contacts, then repaint InfoPanel in main frame.
     * @param phone sanitized user input in PhoneFrame.phoneTextField
     */
    static void addPhone(String phone) {
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        int contactId = contacts.get(selectedContactIndex).getId();
        DBGateway.addPhone(contactId, phone);
        contacts.get(selectedContactIndex).addPhone(phone);
        mainFrame.setInfoPanel();
    }


    /**
     * Show PhoneFrame with currently selected phone in editField
     * set PhoneFrame to edit
     */
    static void showEditPhoneFrame(String oldPhone){
        PhoneFrame editPhoneFrame = new PhoneFrame();
        editPhoneFrame.setFrameToEdit(oldPhone);
        editPhoneFrame.setVisible(true);

    }

    /**
     *  Replace old phone number with new one in DB, App.contacts and view
     *  possible bug: if contact have two or more identical  phone num, this method replace only first of them
     * @param newPhone sanitized user input from PhoneFrame.phoneTextField
     * @param oldPhone stored in PhoneFrame.oldPhone
     */
    static void editPhone(String newPhone, String oldPhone){
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        int contactId = contacts.get(selectedContactIndex).getId();
        DBGateway.editPhone(contactId, newPhone, oldPhone);
        contacts.get(selectedContactIndex).removePhone(oldPhone);
        contacts.get(selectedContactIndex).addPhone(newPhone);
        mainFrame.setInfoPanel();
    }

    /**
     * Delete current phone from DB and view
     * possible bug: if contact have two or more identical  phone num, this method delete only first of them
     * @param phone - come from Mainframe.phoneList
     */
    static void removePhone(String phone) {
        int selectedContactIndex = mainFrame.getSelectedContactIndex();
        int contactId = contacts.get(selectedContactIndex).getId();
        DBGateway.removePhone(contactId, phone);
        contacts.get(selectedContactIndex).removePhone(phone);
        mainFrame.setInfoPanel();
    }


}
