package com.qa.ims.GUI;

import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class MyTableModelListener implements TableModelListener {

    JTable table;
    MyDefaultTableModel tableModel;

    public MyTableModelListener(JTable table) {
        this.table = table;
        this.tableModel = (MyDefaultTableModel) table.getModel();

    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        int index = e.getColumn();
        switch (e.getType()) {
            case TableModelEvent.UPDATE:

                table.getColumnCount();
                Object[] data = new Object[table.getColumnCount()];
                for (int i = 0; i < table.getColumnCount(); i++) {
                    data[i] = table.getValueAt(firstRow, i);
                }
                if (tableModel.getDomain().equals(Domain.CUSTOMER)) {
                    new CustomerDAO().update(new Customer((String[]) data));
                } else if (tableModel.getDomain().equals(Domain.CUSTOMER)){
                    new ItemDAO().update(new Item((String[]) data));
                }

        }

    }
}
