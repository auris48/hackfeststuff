package com.qa.ims.GUI;

import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class MyDateCellEditor extends AbstractCellEditor implements TableCellEditor {
    JDatePickerImpl datePicker;

    public MyDateCellEditor(JDatePickerImpl datePicker) {


    }

    @Override
    public Object getCellEditorValue() {
        return datePicker;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return null;
    }
}
