package Phonebook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactFrame extends JFrame {
    private JPanel addContactPanel;
    private JButton addButton;
    private JButton CancelButton;
    private JTextField firstNameTextField;
    private JTextField secondNameTextField;
    private boolean isEdit = false;

    ContactFrame(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(addContactPanel);
        setTitle("Add contact to list");
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/3-this.getSize().width/2, dim.height/3-this.getSize().height/2);
        pack();

        CancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //form validation there
                boolean isFirstNameValid = true;
                boolean isSecondNameValid = true;
                //----------------------

                if (isFirstNameValid && isSecondNameValid) {
                    if (isEdit) {
                        App.editContact(firstNameTextField.getText(), secondNameTextField.getText());
                    } else {
                        App.AddNewContact(firstNameTextField.getText(), secondNameTextField.getText());
                    }
                    dispose();
                } else {
                    if (!isFirstNameValid) {
                        firstNameTextField.setBorder(BorderFactory.createLineBorder(Color.red));
                    }
                    if (isSecondNameValid) {
                        secondNameTextField.setBorder(BorderFactory.createLineBorder(Color.red));
                    }
                    //paint red border on text fields
                }
            }
        });
    }

    void setFrameToEdit(String oldFirstName, String oldSecondName) {
        isEdit = true;
        setTitle("Edit contact");
        addButton.setText("Save");
        firstNameTextField.setText(oldFirstName);
        secondNameTextField.setText(oldSecondName);

    }

}
