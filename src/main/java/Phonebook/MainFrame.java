package Phonebook;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel MainPanel;
    private JList contactsList;
    private DefaultListModel contactsListModel;
    private JButton removeButton;
    private JButton addButton;
    private JPanel phonePanel;
    private JList phoneList;
    private DefaultListModel phoneListModel;
    private JPanel phoneButtonPanel;
    private JButton addPhoneButton;
    private JButton editPhoneButton;
    private JButton removePhoneButton;
    private JPanel infoPanel;
    private JComboBox groupComboBox;
    private JButton saveGroupButton;
    private JButton editButton;


    MainFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setContentPane(MainPanel);
        setTitle("Phonebook");
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/4-this.getSize().width/2, dim.height/4-this.getSize().height);
        setPanelEnabled(phonePanel, false);
        setPanelEnabled(infoPanel, false);
        pack();

        phoneListModel = new DefaultListModel();
        phoneList.setModel(phoneListModel);

        contactsListModel = new DefaultListModel();
        contactsList.setModel(contactsListModel);
        contactsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                setInfoPanel();
            }
        });



        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.showAddNewContactFrame();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.removeContact(contactsList.getSelectedIndex());
                contactsListModel.remove(contactsList.getSelectedIndex());

            }
        });

        saveGroupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int contactIndex = contactsList.getSelectedIndex();
                String groupName = (String)groupComboBox.getSelectedItem();
                App.saveGroup(groupName, contactIndex);
            }
        });
        addPhoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.showAddPhoneFrame();
            }
        });

        removePhoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String phone = (String) phoneList.getSelectedValue();
                App.removePhone(phone);
            }
        });
        editPhoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String oldPhone = (String) phoneList.getSelectedValue();
                App.showEditPhoneFrame(oldPhone);
            }
        });
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.showEditContactFrame();
            }
        });
    }

    void addContactToView(Contact contact){
        contactsListModel.addElement(contact.getFirstName() + " " + contact.getSecondName());
    }

    void editContactInView(Contact contact, int index) {
        contactsListModel.setElementAt(contact.getFirstName() + " " + contact.getSecondName(), index);
    }

    void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);

        Component[] components = panel.getComponents();

        for(int i = 0; i < components.length; i++) {
            if(components[i].getClass().getName() == "javax.swing.JPanel") {
                setPanelEnabled((JPanel) components[i], isEnabled);
            }

            components[i].setEnabled(isEnabled);
        }
    }

    int getSelectedContactIndex(){
        return contactsList.getSelectedIndex();
    }

    void setInfoPanel(){
        setPanelEnabled(phonePanel, true);
        setPanelEnabled(infoPanel, true);
        int selectedId = contactsList.getSelectedIndex();
        if (selectedId == -1) {
            setPanelEnabled(phonePanel, false);
            setPanelEnabled(infoPanel, false);
            return;
        }
        phoneListModel.clear();
        for(String phone: App.contacts.get(selectedId).getPhones()) {
            phoneListModel.addElement(phone);
        }

        groupComboBox.removeAllItems();

        for (String group: App.groups){
            groupComboBox.addItem(group);
            if (group.equals(App.contacts.get(selectedId).getGroup())) {
                groupComboBox.setSelectedItem(group);
            }
        }
    }

}
