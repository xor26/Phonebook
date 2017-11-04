package Phonebook;

import java.sql.*;
import java.util.ArrayList;

class DBGateway {

    private static Connection connection;
    private static Statement statement;

    static void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:Phonebook.sqlite");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void saveContactToDB(Contact contact) {
        try {
            String sql = "UPDATE contacts SET " +
                    "firstName = \"" + contact.getFirstName() + "\", " +
                    "secondName = \"" + contact.getSecondName() + "\" " +
                    "WHERE id = " + contact.getId();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all existing info from DB by contact id and create object of Contact-class
     * @param id id in contacts-table
     * @return  object of Contact-class
     */
    static Contact getContactFromDB(int id) {
        Contact cont = null;
        try {
            String sql = "SELECT contacts.firstName, contacts.secondName, groups.groupName " +
                    "FROM contacts JOIN groups ON contacts.groupId = groups.id " +
                    "WHERE contacts.id =" + id;
            ResultSet rs = statement.executeQuery(sql);

            cont = new Contact();
            cont.setId(id);
            cont.setFirstName( rs.getString("firstName"));
            cont.setSecondName(rs.getString("secondName"));
            cont.setGroup(rs.getString("groupName"));

            sql = "SELECT phone FROM phones WHERE contactId = " + id;
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                cont.addPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cont;
    }



    static ArrayList<Contact> getContactsList(){

        ArrayList<Contact> contactsList = new ArrayList<Contact>();
        try {
            ResultSet rs = statement.executeQuery("SELECT id FROM contacts");
            ArrayList<Integer> idList = new ArrayList<Integer>();
            while (rs.next()) {
                idList.add(rs.getInt("id"));
            }
            //for some reason i cant create arrayList of Contacts in first cycle
            for (Integer id: idList) {
                contactsList.add(getContactFromDB(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

    ArrayList<String> getGroupList(){
        ArrayList<String> groupList = new ArrayList<String>();
        try {
            ResultSet rs = statement.executeQuery("SELECT groupName FROM groups");
            while (rs.next()){
                groupList.add(rs.getString("groupName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    static void insertContact(Contact cont) {
        String sql = "INSERT INTO contacts (firstName, secondName) " +
                "VALUES (" +
                    "\"" + cont.getFirstName() +"\", " +
                    "\"" + cont.getSecondName() +"\"" +
                ")";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    static void deleteContact(int id){
        try {
            statement.executeUpdate("DELETE FROM contacts WHERE id = " + id);
            statement.executeUpdate("DELETE FROM phones WHERE contactId = " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void setGroup(int contactId, String groupName) {
        try {
            ResultSet rs = statement.executeQuery("SELECT id FROM groups WHERE groupName = \"" + groupName + "\"");
            int groupId = rs.getInt("id");
            statement.executeUpdate("UPDATE contacts SET groupId = " + groupId + " WHERE id = " + contactId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addPhone(int contactId, String phone){
        try {
            statement.executeUpdate("INSERT INTO phones (contactId, phone) " +
                    "VALUES ("+ contactId + ", \"" + phone + "\")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void editPhone(int contactId,  String newPhone, String oldPhone) {
        try {
            statement.executeUpdate("UPDATE phones SET " +
                    "phone = " + "\"" + newPhone + "\"" +
                    " WHERE contactId =" + contactId +
                    " AND phone = \"" + oldPhone + "\"");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void removePhone(int contactId, String phone) {
        try {
            statement.executeUpdate("DELETE FROM phones WHERE contactId = " + contactId+ " AND " +
                    " phone = \"" + phone + "\"");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
