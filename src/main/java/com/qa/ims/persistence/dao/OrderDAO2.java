package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.persistence.domain.OrderDetail;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class OrderDAO2 implements Dao<Order> {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Order> readAll() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orders")) {
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = modelFromResultSet(resultSet);
                order.setOrderDetailList(readOrderDetails(order));
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Order> returnOrdersWithCustomerID(Long id) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders WHERE customer_id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = modelFromResultSet(resultSet);
                    order.setOrderDetailList(readOrderDetails(order));
                    orders.add(order);
                }
                ;

                return orders;
            }

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
                order.setOrderDetailList(readOrderDetails(order));
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
            if (!order.getOrderDetailList().isEmpty()) {
                createOrderDetail(order);
            }
            return readLatest();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }


    public void createOrderDetail(Order order) {
        List<OrderDetail> existantOrderDetailsInSQL = checkIfOrderDetailExists(order);
        List<OrderDetail> nonexistantOrderDetailsInSQL = order.getOrderDetailList().stream()
                .filter(orderDetail -> !existantOrderDetailsInSQL.contains(orderDetail)).collect(Collectors.toList());

        if (!nonexistantOrderDetailsInSQL.isEmpty()) {
            try (Connection connection = DBUtils.getInstance().getConnection();
                 PreparedStatement statement = connection
                         .prepareStatement("INSERT INTO order_items (order_id, item_id, item_quantity) VALUES (?, ?, ?)")) {


                List<OrderDetail> orderDetail = nonexistantOrderDetailsInSQL;
                orderDetail.stream()
                        .forEach(orderItem -> {
                            try {
                                statement.setLong(1, order.getId());
                                statement.setLong(2, orderItem.getItem().getId());
                                statement.setInt(3, orderItem.getQuantity());
                                statement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (Exception e) {
                LOGGER.debug(e);
                LOGGER.error(e.getMessage());
            }

        } else {
            existantOrderDetailsInSQL.forEach(item->updateOrderItemsQuantity(order, item.getItem().getId(), item.getQuantity()));
        }
    }

    public List<OrderDetail> checkIfOrderDetailExists(Order order) {
        List<OrderDetail> orderDetailList = new ArrayList<>();

        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("SELECT COUNT(order_id) AS ITEMS FROM order_items WHERE item_id=? AND order_id=?")) {
            for (OrderDetail orderDetail : order.getOrderDetailList()) {
                statement.setLong(1, orderDetail.getItem().getId());
                statement.setLong(2, order.getId());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getInt("ITEMS") > 0) {
                        orderDetailList.add(orderDetail);
                    }
                    ;
                }

            }
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return orderDetailList;
    }

    public List<OrderDetail> readOrderDetails(Order order) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM order_items WHERE order_id = ?")) {
            statement.setLong(1, order.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orderDetails.addAll(getOrderDetails(resultSet));
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
                     .prepareStatement("UPDATE orders SET customer_id = ?, order_date = ?, order_dueDate = ?, order_cost = ? WHERE id = ?")) {
            statement.setString(1, order.getCustomerID().toString());
            statement.setString(2, order.getOrderDate().toString());
            statement.setString(3, order.getOrderDueDate().toString());
            statement.setDouble(4, order.getOrderCost());
            statement.setLong(5, order.getId());
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
             PreparedStatement statement = connection.prepareStatement("DELETE FROM orders WHERE id = ?")) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return 0;
    }

    public Order deleteOrderItemsByID(Order order, long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM order_items WHERE item_id = ? AND order_id = ?")) {
            statement.setLong(1, id);
            statement.setLong(2, order.getId());
            statement.executeUpdate();
            order.setOrderDetailList(readOrderDetails(order));
            return order;
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public Order updateOrderItemsQuantity(Order order, long id, int quantity) {
        try (Connection connection = DBUtils.getInstance().getConnection(); //UPDATE orders SET customer_id = ?, order_date = ?, order_dueDate = ? WHERE id = ?
             PreparedStatement statement = connection.prepareStatement("UPDATE order_items SET item_quantity = ? WHERE item_id = ? AND order_id = ?")) {
            statement.setInt(1, quantity);
            statement.setLong(2, id);
            statement.setLong(3, order.getId());
            statement.executeUpdate();
            order.setOrderDetailList(readOrderDetails(order));
            return order;
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }


    public List<OrderDetail> getOrderDetails(ResultSet resultSet) throws SQLException {
        List<OrderDetail> orderDetails = new ArrayList<>();
        Long orderDetailID = resultSet.getLong("id");
        Long item_id = resultSet.getLong("item_id");
        int item_quantity = resultSet.getInt("item_quantity");
        orderDetails.add(new OrderDetail(orderDetailID, new ItemDAO().read(item_id), item_quantity));
        return orderDetails;
    }

    @Override
    public Order modelFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long customerID = resultSet.getLong("customer_id");
        LocalDate orderDate = LocalDate.parse(resultSet.getString("order_date"));
        LocalDate orderDueDate = LocalDate.parse(resultSet.getString("order_dueDate"));
        ;
        Double orderCost = resultSet.getDouble("order_cost");
        return new Order(id, customerID, orderDate, orderDueDate, orderCost);
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

