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
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM items")) {
            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(modelFromResultSet(resultSet));
            }
            return items;
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Item read(Long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM items WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return modelFromResultSet(resultSet);
            }
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Item create(Item item) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO items(item_name, item_stockdate, item_description, item_stock, item_price) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getItemStockDate() != null ? item.getItemStockDate().toString() : LocalDate.ofEpochDay(0).toString());
            statement.setString(3, item.getItemDescription());
            statement.setString(4, String.valueOf(item.getItemStock()));
            statement.setString(5, String.valueOf(item.getItemPrice()));
            statement.executeUpdate();
            return readLatest();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Item update(Item item) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE items SET item_name = ?, item_stockdate = ?, item_description = ?, item_stock = ?, item_price = ? WHERE id = ?")) {
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getItemStockDate().toString());
            statement.setString(3, item.getItemDescription());
            statement.setString(4, String.valueOf(item.getItemStock()));
            statement.setString(5, String.valueOf(item.getItemPrice()));
            statement.setString(6, String.valueOf(item.getId()));
            statement.executeUpdate();
            return read(item.getId());
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public int delete(long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id = ?")) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public Item modelFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String itemName = resultSet.getString("item_name");
        String itemDescription = resultSet.getString("item_description");
        LocalDate itemStockDate;
        try {
             itemStockDate = LocalDate.parse(resultSet.getString("item_stockdate"));
        } catch (NullPointerException e){
            itemStockDate=null;
        }


        int itemStock = resultSet.getInt("item_stock");
        double itemPrice = resultSet.getDouble("item_price");
        return new Item(id, itemName, itemStockDate, itemDescription, itemStock, itemPrice);
    }

    public Item readLatest() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM items ORDER BY id DESC LIMIT 1")) {
            resultSet.next();
            return modelFromResultSet(resultSet);
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}

