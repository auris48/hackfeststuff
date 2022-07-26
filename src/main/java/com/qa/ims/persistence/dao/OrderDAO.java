package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO implements Dao<Order> {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Order> readAll() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM order")) {
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(modelFromResultSet(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }



    @Override
    public Order read(Long id) {
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
    public Order create(Order order) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO order VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, String.valueOf(order.getCustomer_id()));
            statement.setString(2, order.getOrderDate().toString());
            statement.setString(3, order.getOrderDueDate().toString());
            statement.setString(4, String.valueOf(order.getOrderCost()));
            statement.executeUpdate();
            return readLatest();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public Map<Item, Integer> createOrderDetail(Order order) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO order_items VALUES (?, ?, ?)")) {


            Map<Item, Integer> orderDetail = order.getOrderDetail();
            orderDetail.keySet()
                                  .stream()
                                  .forEach(item -> {
                                      try {
                                          statement.setString(1, String.valueOf(order.getId()));
                                          statement.setString(1, String.valueOf(item.getId()));
                                          statement.setString(2, (String.valueOf(orderDetail.get(item))));
                                      } catch (SQLException e) {
                                          e.printStackTrace();
                                      }
                                  });

            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }


    public Map<Item, Integer> readOrderDetail(Order order) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM order_items WHERE order_id = ?")) {
            statement.setLong(1, order.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return getOrderDetails(resultSet);
            }
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }


    @Override
    public Order update(Order order) {
        /*try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE items SET item_name = ?, item_stockdate = ?, item_description = ?, item_stock = ? item_price = ? WHERE id = ?")) {
            statement.setString(1, order.getId());
            statement.setString(2, item.getItemStockDate().toString());
            statement.setString(3, item.getItemDescription());
            statement.executeUpdate();
            return read(item.getId());
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }*/

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

    public Map<Item, Integer> getOrderDetails(ResultSet resultSet) throws SQLException {
        Map<Item, Integer> orderDetails = new HashMap<>();
        Long item_id = resultSet.getLong("item_id");
        int item_quantity = resultSet.getInt("item_quantity");
        orderDetails.put(new ItemDAO().read(item_id), item_quantity);
        return orderDetails;
    }

    @Override
    public Order modelFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long customerID = resultSet.getLong("customer_id");
        LocalDate orderDate = LocalDate.parse(resultSet.getString("order_date"));
        LocalDate orderDueDate = LocalDate.parse(resultSet.getString("order_due_date"));;
        return new Order(id, customerID, orderDate, orderDueDate);
    }

    public Order readLatest() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orders ORDER BY id DESC LIMIT 1")) {
            resultSet.next();
            return modelFromResultSet(resultSet);
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }
    }

