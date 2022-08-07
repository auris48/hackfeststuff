package com.qa.ims.GUI;

import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.domain.Customer;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCustomerForm  extends JFrame{
    private JLabel lblCustomer;
    private JTextField txtFirstName;
    private JTextField txtSurname;
    private JButton btnAdd;
    private JLabel lblCustomerName;
    private JPanel pnlTitle;
    private JPanel pnlAddPanel;
    private JPanel pnlMainPanel;
    private JTextField txtAddress;
    private JTextField txtPostcode;
    private JTextField txtPhone;

    public AddCustomerForm(MainFrame mainFrame) throws HeadlessException {
        setSize(300, 300);
        setVisible(true);
        add(pnlMainPanel);
        pack();
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer customer = new CustomerDAO().create(new Customer(txtFirstName.getText(), txtSurname.getText(), txtAddress.getText(), txtPostcode.getText(), txtPhone.getText()));
                mainFrame.getCustomerModel().addRecord(customer);
            }
        });
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

}
