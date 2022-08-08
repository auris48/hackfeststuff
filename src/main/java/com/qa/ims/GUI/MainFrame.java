package com.qa.ims.GUI;
import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Properties;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import org.jdatepicker.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

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
        tblItems.getModel().addTableModelListener(new MyTableModelListener(tblItems));
        tblCustomers.setComponentPopupMenu(popupMenu);
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        DatePickerCellEditor dateEditor=new DatePickerCellEditor();
        dateEditor.setFormats(new SimpleDateFormat("YYYY-MM-DD"));
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        TableColumn dateColumn = tblItems.getColumn("Item Stock Date");
        dateColumn.setCellEditor(dateEditor);

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
                int firstRow = Arrays.stream(tblCustomers.getSelectedRows()).min().orElse(Integer.MIN_VALUE);
                int lastRow = Arrays.stream(tblCustomers.getSelectedRows()).max().orElse(Integer.MAX_VALUE);
                System.out.println(firstRow+":"+lastRow);
                for (int i = lastRow; i >=firstRow; i--) {
                    customerModel.getDao().delete(Long.parseLong((String) customerModel.getValueAt(i, 0)));
                    customerModel.removeRow(i);
                }
                customerModel.fireTableRowsDeleted(firstRow, lastRow);
                repaint();
            }
        });


        btnAddItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = itemModel.getRowCount();
                itemModel.addRow(new ItemDAO().create(new Item()).toString().split(","));
                itemModel.fireTableRowsInserted(row, row);
            }
        });
        tblItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = tblItems.rowAtPoint(e.getPoint());
                int col = tblItems.columnAtPoint(e.getPoint());
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

}
