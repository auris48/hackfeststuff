package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO implements Dao<Item> {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Item> readAll() {
        return null;
    }

    @Override
    public Item read(Long id) {
        return null;
    }

    @Override
    public Item create(Item item) {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public Item modelFromResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }
    }

