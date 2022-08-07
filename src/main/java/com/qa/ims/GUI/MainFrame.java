package com.qa.ims.GUI;


import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.Arrays;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tbpnMain;
    private JTable tblCustomers;
    private JScrollPane pnlDrivers;
    private JScrollPane pnlOrders;
    private JScrollPane pnlCustomer;
    private JButton addCustomerButton;
    private JPanel pnlCustomers;
    private JButton deleteCustomerButton;
    private JPanel pnlItems;
    private JButton btnRemoveItem;
    private JButton btnAddItem;
    private JScrollPane scpnTableHolder;
    private JTable tblItems;
    private AddCustomerForm addCustomerForm;
    private MyDefaultTableModel customerModel;
    private MyDefaultTableModel itemModel;

    public MainFrame() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        setTitle("Qommon Logistics");
        setVisible(true);
        setSize(700, 700);
        add(tbpnMain);
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new JMenuItem("Delete"));
        tblCustomers.getSelectedRow();
        customerModel = new MyDefaultTableModel<Customer>(new CustomerDAO(), Domain.CUSTOMER);
        itemModel = new MyDefaultTableModel<Item>(new ItemDAO(), Domain.ITEM);
        customerModel.createTable();
        itemModel.createTable();
        tblCustomers.setModel(customerModel);
        tblItems.setModel(itemModel);
        tblCustomers.getModel().addTableModelListener(new MyTableModelListener(tblCustomers));

        tblCustomers.setComponentPopupMenu(popupMenu);

        addCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = customerModel.getRowCount();
                customerModel.addRow(new CustomerDAO().create(new Customer()).toString().split(","));
                customerModel.fireTableRowsInserted(row, row);
                //customerModel.addRow(new CustomerDAO().create(new Customer()).toString().split(","));
            }
        });

        deleteCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Arrays.stream(tblCustomers.getSelectedRows()).forEach(row -> new CustomerDAO().delete(Long.parseLong((String) tblCustomers.getValueAt(row, 0))));
                int firstRow = Arrays.stream(tblCustomers.getSelectedRows()).min().orElse(Integer.MIN_VALUE);
                int lastRow = Arrays.stream(tblCustomers.getSelectedRows()).max().orElse(Integer.MAX_VALUE);
                customerModel.fireTableRowsDeleted(firstRow, lastRow);
                customerModel.updateTable();
            }
        });
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new MainFrame();
    }

    public MyDefaultTableModel getCustomerModel() {
        return customerModel;
    }

    public DefaultTableModel updateTable() {

        DefaultTableModel tblModel = new DefaultTableModel();
        Field[] fields = Customer.class.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> tblModel.addColumn(field.getName()));
        Object[][] data =
                new CustomerDAO()
                        .readAll()
                        .stream()
                        .map(customer ->
                                new Object[]{customer.getId(), customer.getFirstName(), customer.getSurname(), customer.getAddress(), customer.getPostcode(), customer.getPhone()})
                        .toArray(size -> new Object[size][tblModel.getColumnCount()]);

        Arrays.stream(data).forEach(datum -> tblModel.addRow(datum));
        return tblModel;
    }
/*
    public DefaultTableModel createTableModel(Dao dao, Class object) throws NoSuchMethodException {

        DefaultTableModel tblModel = new DefaultTableModel();
        Field[] fields = object.getDeclaredFields();

        Arrays.stream(fields).forEach(field -> tblModel.addColumn(field.getName()));


        Object[][] data = dao
                .readAll()
                .stream()
                .map(entry -> new Object[]{Arrays.stream(fields).forEach(field -> try {object.getClass().getMethod(field.getName()).invoke(object).toString();}
                                                    catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                                    e.printStackTrace();}
                }))
                .toArray(size -> new Object[size][tblModel.getColumnCount()]);


        Arrays.stream(data).forEach(datum -> tblModel.addRow(datum));
        return tblModel;
    }
*/

    public JTable getTblCustomers() {
        return tblCustomers;
    }
}
