package com.qa.ims.GUI;

import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.Dao;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.DomainType;
import com.qa.ims.persistence.domain.Item;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

enum Domain {CUSTOMER, ITEM, DRIVER, ORDER, DELIVERY};

public class MyDefaultTableModel<T extends DomainType> extends DefaultTableModel {
    private final Dao dao;
    private List<T> list;
    private Domain domain;

    public MyDefaultTableModel(Dao dao, Domain domain) {
        this.dao = dao;
        this.domain = domain;
    }

    public void deleteRecord(int i) {
        removeRow(i);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

    public MyDefaultTableModel createTable() {
        list = dao.readAll();
        switch (domain) {
            case CUSTOMER:
                this.setColumnIdentifiers(Customer.getAllFields());
                break;
            case ITEM:
                this.setColumnIdentifiers(Item.getAllFields());
                break;
            case ORDER:
            case DRIVER:
            case DELIVERY:
        }

        Object[][] data = list.stream().map
                        (entry -> entry.toString().split(","))
                .toArray(size -> new Object[list.size()][size]);

        for (Object[] datum : data) {

            addRow(datum);
        }
        return this;
    }

    public void addRecord(T t) {
        addRow(t.toString().split(","));
    }

    public void updateTable() {
        setRowCount(0);
        createTable();
    }

    public Domain getDomain() {
        return domain;
    }

    public Dao getDao() {
        return dao;
    }
}
