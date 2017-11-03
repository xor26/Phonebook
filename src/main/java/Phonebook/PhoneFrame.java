package Phonebook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhoneFrame extends JFrame {

    private JPanel addPhonePanel;
    private JTextField phoneTextField;
    private JButton addButton;
    private JButton cancelButton;
    private boolean isEdit = false;
    private String oldPhone;

    PhoneFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(addPhonePanel);
        setTitle("Add contact to list");

        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 3 - this.getSize().width / 2, dim.height / 3 - this.getSize().height / 2);
        pack();

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //regExp validation phone will be there
                boolean isPhoneValid = true;
                //----------------------

                if (isPhoneValid) {
                    if (isEdit) {
                        App.editPhone(phoneTextField.getText(), oldPhone);
                    } else {
                        App.addPhone(phoneTextField.getText());
                    }
                    dispose();
                } else {
                    phoneTextField.setBorder(BorderFactory.createLineBorder(Color.red));
                }
            }
        });
    }

    void setFrameToEdit(String oldPhone) {
        isEdit = true;
        setTitle("Edit phone");
        addButton.setText("Edit");
        phoneTextField.setText(oldPhone);
        this.oldPhone = oldPhone;

    }


}
