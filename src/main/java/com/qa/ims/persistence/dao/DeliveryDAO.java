package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Delivery;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DeliveryDAO implements Dao<Delivery> {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Delivery> readAll() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM delivery")) {
            List<Delivery> deliveries = new ArrayList<>();
            while (resultSet.next()) {
                Delivery delivery = modelFromResultSet(resultSet);
                delivery.setOrders(readDeliveryDetail(delivery));
                deliveries.add(delivery);
            }
            return deliveries;
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Delivery read(Long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM delivery WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                Delivery delivery = modelFromResultSet(resultSet);
                delivery.setOrders(readDeliveryDetail(delivery));
                return delivery;
            }
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Delivery create(Delivery delivery) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO delivery (driver_id) VALUES (?)")) {
            statement.setLong(1, delivery.getDriver().getId());
            statement.executeUpdate();
            if (!delivery.getOrders().isEmpty()) {
                createDeliveryDetail(delivery);
            }
            return readLatest();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public Delivery createDeliveryDetail(Delivery delivery) {
        List<Order> nonExistantOrders = checkIfOrderExists(delivery);

        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO delivery_orders (delivery_id, order_id) VALUES (?, ?)")) {
            nonExistantOrders.stream()
                    .forEach(order -> {
                        try {
                            statement.setLong(1, delivery.getId());
                            statement.setLong(2, order.getId());
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }

        return null;
    }

    public List<Order> checkIfOrderExists(Delivery delivery) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("SELECT COUNT(delivery_id) AS ORDERS FROM delivery_orders WHERE order_id=? AND delivery_id=?")) {
            for (Order order : delivery.getOrders()) {
                statement.setLong(1, order.getId());
                statement.setLong(2, delivery.getId());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getInt("ORDERS") == 0) {
                        orders.add(order);
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return orders;
    }

    public List<Order> readDeliveryDetail(Delivery delivery) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM delivery_orders WHERE delivery_id = ?")) {
            statement.setLong(1, delivery.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.addAll(getDeliveryDetail(resultSet));
                }
                return orders;
            }
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Delivery update(Delivery delivery) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE delivery SET driver_id = ? WHERE id = ?")) {
            statement.setLong(1, delivery.getDriver().getId());
            statement.setLong(2, delivery.getId());
            statement.executeUpdate();
            return read(delivery.getId());
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }

        return null;
    }

    @Override
    public int delete(long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM delivery WHERE id = ?")) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return 0;
    }

    public Delivery deleteDeliveryItemsByID(Delivery delivery, long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM delivery_orders WHERE order_id = ? AND delivery_id = ?")) {
            statement.setLong(1, id);
            statement.setLong(2, delivery.getId());
            statement.executeUpdate();
            delivery.setOrders(readDeliveryDetail(delivery));
            return delivery;
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public List<Order> getDeliveryDetail(ResultSet resultSet) throws SQLException {
        List<Order> orders = new ArrayList<>();
        ;
        Long orderID = resultSet.getLong("order_id");
        orders.add(new OrderDAO().read(orderID));
        return orders;
    }

    @Override
    public Delivery modelFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long driverID = resultSet.getLong("driver_id");
        return new Delivery(id, new DriverDAO().read(driverID));
    }


    public Delivery readLatest() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM delivery ORDER BY id DESC LIMIT 1")) {
            resultSet.next();
            return modelFromResultSet(resultSet);
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}



