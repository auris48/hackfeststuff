package com.qa.ims.GUI;

import com.qa.ims.persistence.domain.DomainType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainAddMenu<T extends DomainType> extends JFrame {
    T t;
    JPanel titlePanel;
    JPanel formPanel;
    public DomainAddMenu(T t) throws HeadlessException {
        titlePanel=new JPanel();
        add(titlePanel);
        add(formPanel);
        setVisible(true);

        List<Component> componentList = new ArrayList<>();

        Arrays.stream(t.getFields()).forEach(field -> componentList.add(new JLabel(field)));

    }
}
