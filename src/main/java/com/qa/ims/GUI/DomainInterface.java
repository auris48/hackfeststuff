package com.qa.ims.GUI;

import java.util.List;

public interface DomainInterface<T> {

    List<T> readAll();

    T create();

    T update();

    int delete();

}
