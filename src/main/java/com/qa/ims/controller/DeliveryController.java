package com.qa.ims.controller;

import com.qa.ims.persistence.dao.DeliveryDAO;
import com.qa.ims.persistence.dao.DriverDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.*;
import com.qa.ims.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

/**
 * Takes in Order details for CRUD functionality
 */


public class DeliveryController implements CrudController<Delivery> {

    public static final Logger LOGGER = LogManager.getLogger();
    private OrderDAO orderDao;
    private DeliveryDAO deliveryDAO;
    private DriverDAO driverDAO;
    private Utils utils;

    public DeliveryController(OrderDAO orderDao, DeliveryDAO deliveryDAO, DriverDAO driverDAO, Utils utils) {
        super();
        this.orderDao = orderDao;
        this.deliveryDAO = deliveryDAO;
        this.driverDAO = driverDAO;
        this.utils = utils;
    }

    @Override
    public List<Delivery> readAll() {
        List<Delivery> deliveries = deliveryDAO.readAll();
        for (Delivery delivery : deliveries) {
            LOGGER.info("------------------------------------------------------------------------------------------------------------");
            LOGGER.info(delivery);

        }
        return deliveries;
    }

    @Override
    public Delivery create() {
        LOGGER.info("Do you want to add a driver? (Yes/No)");
        Delivery delivery = new Delivery();

        if (utils.getString().equalsIgnoreCase("Yes")) {
            LOGGER.info("Please enter driver id:");
            Long driverID = utils.getLong();
            delivery.setDriver(driverDAO.read(driverID));
        }
        delivery = deliveryDAO.create(delivery);
        return delivery;
    }

    public Delivery addOrdersToDelivery(Delivery delivery) {
        boolean adding = true;

        while (adding) {
            orderDao.readAll().forEach(order -> LOGGER.info(order.toString()));
            LOGGER.info("Please enter ID of order to add to the delivery");
            Long id = utils.getLong();
            Order order = orderDao.read(id);
                if (!delivery.containsOrderWithID(order)) {
                      delivery.addToOrderList(order);
                } else{
                    LOGGER.info("Order already has been added");
                }


        LOGGER.info("Would you like to add any more orders? (Yes/No)");
        if (utils.getString().equalsIgnoreCase("No")) {
            adding = false;
        }
        }
        return delivery;
    }

    @Override
    public Delivery update() {
        LOGGER.info("Please enter the id of the delivery you would like to update");
        Long id = utils.getLong();
        LOGGER.info("Please enter new driver");
        Long driverID = utils.getLong();
        Delivery delivery = deliveryDAO.update(new Delivery(id, driverDAO.read(driverID)));
        LOGGER.info("Delivery updated");
        LOGGER.info("Would you like to update orders belonging to this delivery? (Yes/No)");
        delivery = deliveryDAO.read(id);
        if (utils.getString().equalsIgnoreCase("Yes")) {
            updateOrderDetails(delivery);
            deliveryDAO.createDeliveryDetail(delivery);
        }

        return delivery;
    }

    public Delivery updateOrderDetails(Delivery delivery) {
        boolean amending = true;
        while (amending) {
            LOGGER.info("Would you like to change existing  order or add more orders? (Add/Change)");
            if (utils.getString().equalsIgnoreCase("Add")) {
                addOrdersToDelivery(delivery);
            } else {
                LOGGER.info("Here are all orders belonging to this delivery: ");
                delivery.getOrders().stream().forEach(System.out::println);
                LOGGER.info("Please enter order id");
                Long orderID = utils.getLong();
                LOGGER.info("Would you like to delete order or change id? (Delete/Change)");
                if (utils.getString().equalsIgnoreCase("Delete")) {
                    deliveryDAO.deleteDeliveryItemsByID(delivery, orderID);
                    delivery.setOrders(deliveryDAO.readDeliveryDetail(delivery));
                }
            }
            LOGGER.info("Do you want to keep changing order items? (Yes/No)");
            if (utils.getString().equalsIgnoreCase("No")) {
                break;
            }
        }

        return delivery;
    }

    @Override
    public int delete() {
        LOGGER.info("Please enter the id of the delivery you would like to delete");
        Long id = utils.getLong();
        return orderDao.delete(id);
    }
}
