package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class OrderDAO implements Dao<Order> {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Order> readAll() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orders")) {
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = modelFromResultSet(resultSet);
                order.setOrderDetail(readOrderDetails(order));
                orders.add(order);
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
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                Order order = modelFromResultSet(resultSet);
                order.setOrderDetail(readOrderDetails(order));
                return order;
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
                     .prepareStatement("INSERT INTO orders (customer_id, order_date, order_dueDate, order_cost) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, String.valueOf(order.getCustomerID()));
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

    public void createOrderDetail(Order order) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO order_items (order_id, item_id, item_quantity) VALUES (?, ?, ?)")) {


            Map<Item, Integer> orderDetail = order.getOrderDetail();
            orderDetail.keySet()
                                  .stream()
                                  .forEach(item -> {
                                      try {
                                          statement.setString(1, String.valueOf(order.getId()));
                                          statement.setString(2, String.valueOf(item.getId()));
                                          statement.setString(3, (String.valueOf(orderDetail.get(item))));
                                          statement.executeUpdate();
                                      } catch (SQLException e) {
                                          e.printStackTrace();
                                      }
                                  });
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
    }


    public Map<Item, Integer> readOrderDetails(Order order) {
        Map<Item, Integer> orderDetails = new HashMap<>();

        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM order_items WHERE order_id = ?")) {
            statement.setLong(1, order.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    orderDetails.putAll(getOrderDetails(resultSet));
                }
                return orderDetails;
            }
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }


    @Override
    public Order update(Order order) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE orders SET customer_id = ?, order_date = ?, order_dueDate = ? WHERE id = ?")) {
            statement.setString(1, order.getCustomerID().toString());
            statement.setString(2, order.getOrderDate().toString());
            statement.setString(3, order.getOrderDueDate().toString());
            statement.setString(4, order.getId().toString());;
            statement.executeUpdate();
            return read(order.getId());
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }

        return null;
    }

    @Override
    public int delete(long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM orders WHERE id = ? AND DELETE from order_items where order_id = ?")) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            return statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return 0;
    }

    public Order deleteOrderItemsByID(Order order, long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE * FROM order_items WHERE id = ? AND order_id = ?")) {
            statement.setLong(1, id);
            statement.setLong(2, order.getId());
            order.setOrderDetail(readOrderDetails(order));
            return order;
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public Order updateOrderItemsQuantity(Order order, long id, int quantity) {
        try (Connection connection = DBUtils.getInstance().getConnection(); //UPDATE orders SET customer_id = ?, order_date = ?, order_dueDate = ? WHERE id = ?
             PreparedStatement statement = connection.prepareStatement("UPDATE order_items SET item_quantity WHERE item_id = ? AND order_id = ?")) {
            statement.setInt(1, quantity);
            statement.setLong(2, id);
            statement.setLong(3, order.getId());
            order.setOrderDetail(readOrderDetails(order));
            return order;
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
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
        LocalDate orderDueDate = LocalDate.parse(resultSet.getString("order_dueDate"));;
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

