package com.qa.ims.controller;

import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.persistence.domain.OrderDetail;
import com.qa.ims.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

/**
 * Takes in Order details for CRUD functionality
 */


public class OrderController implements CrudController<Order> {

    public static final Logger LOGGER = LogManager.getLogger();
    private OrderDAO orderDao;
    private ItemDAO itemDao;
    private Utils utils;

    public OrderController(OrderDAO orderDao, Utils utils) {
        super();
        this.orderDao = orderDao;
        this.itemDao=new ItemDAO();
        this.utils = utils;
    }

    @Override
    public List<Order> readAll() {
        List<Order> Orders = orderDao.readAll();
        for (Order Order : Orders) {
            LOGGER.info("------------------------------------------------------------------------------------------------------------");
            LOGGER.info(Order);

        }
        return Orders;
    }

    @Override
    public Order create() {
        LOGGER.info("Please enter order customer id");
        Long customerID = utils.getLong();
        LOGGER.info("Please enter order due date");
        LocalDate orderDueDate = utils.getLocalDate();
        Order order = orderDao.create(new Order(customerID, LocalDate.now(), orderDueDate));
        LOGGER.info("Would you like to add items to order? (Yes/No)");
        if (utils.getString().equalsIgnoreCase("Yes")){
            addItemsToOrder(order);
            orderDao.createOrderDetail(order);
            order.calculateOrderCost();
            orderDao.update(order);
        }
        return order;
    }

    public Order
    addItemsToOrder(Order order) {
        boolean adding = true;

        while (adding) {
            itemDao.readAll().forEach(item -> LOGGER.info(item.toString()));
            LOGGER.info("Please enter ID of item to add to the order");
            Long id = utils.getLong();
            Item item = itemDao.read(id);
            LOGGER.info("Please enter quantity");
            int quantity = utils.getInt();
            if (item.getItemStock() >= quantity) {
                if (item.getItemStock() == quantity){
                    item.setItemStock(0);
                    itemDao.update(item);
                    if(!order.containsItemWithID(item)){
                        order.getOrderDetailList().add(new OrderDetail(item, quantity));
                    }else{
                        order.getExistingOrderDetail(item).addQuantity(quantity);
                    }
                } else{
                    item.setItemStock(item.getItemStock() - quantity);
                    itemDao.update(item);
                    if(!order.containsItemWithID(item)){
                        order.getOrderDetailList().add(new OrderDetail(item, quantity));
                    } else {
                        order.getExistingOrderDetail(item).addQuantity(quantity);
                    }
                }
            } else {
                LOGGER.info("You're trying to order more than the stock");
            }

            LOGGER.info("Would you like to add any more items? (Yes/No)");
            if (utils.getString().equalsIgnoreCase("No")) {
                adding = false;
            }
        }
        return order;
    }


    @Override
    public Order update() {
        LOGGER.info("Please enter the id of the order you would like to update");
        Long id = utils.getLong();
        LOGGER.info("Please enter new customer id");
        Long customerID = utils.getLong();
        LOGGER.info("Please enter new order stock date");
        LocalDate orderStockDate = LocalDate.parse(utils.getString());
        LOGGER.info("Please enter new order due date");
        LocalDate orderDueDate = LocalDate.parse(utils.getString());;
        Order order = orderDao.update(new Order(id, customerID, orderStockDate, orderDueDate));
        LOGGER.info("Order updated");
        LOGGER.info("Would you like to update items belonging to order? (Yes/No)");
        order=orderDao.read(id);
        if (utils.getString().equalsIgnoreCase("Yes")) {
            updateOrderDetails(order);
            orderDao.createOrderDetail(order);
            order.calculateOrderCost();
            orderDao.update(order);
        }

        return order;
    }

    public Order updateOrderDetails(Order order) {
        boolean amending=true;
        while(amending){
            LOGGER.info("Would you like to change existing item order or add more items? (Add/Change)");
            if (utils.getString().equalsIgnoreCase("Add")){
                addItemsToOrder(order);
            } else {
                LOGGER.info("Here are all items belonging to this order: ");
                order.printOrderDetails();
                LOGGER.info("Please enter item id");
                Long itemID = utils.getLong();
                LOGGER.info("Would you like to delete item or change quantity? (Delete/Change)");
                if (utils.getString().equalsIgnoreCase("Delete")){
                    orderDao.deleteOrderItemsByID(order, itemID);
                    order.setOrderDetailList(orderDao.readOrderDetails(order));
                } else {
                    Item item = itemDao.read(itemID);
                    LOGGER.info("Enter new quantity");
                    int quantity = utils.getInt();
                    if (item.getItemStock()>=quantity){
                        orderDao.updateOrderItemsQuantity(order, itemID, quantity);
                        item.setItemStock(item.getItemStock()-quantity);
                        order.setOrderDetailList(orderDao.readOrderDetails(order));
                    } else {
                        LOGGER.info("Not enough items to change to that amount");
                    }

                }
            }
            LOGGER.info("Do you want to keep changing order items? (Yes/No)");
            if(utils.getString().equalsIgnoreCase("No")){
                break;
            }
        }

        return order;
    }

    @Override
    public int delete() {
        LOGGER.info("Please enter the id of the order you would like to delete");
        Long id = utils.getLong();
        Order order = orderDao.read(id);
        order.getOrderDetailList().forEach(orderDetail -> {
            Item item = orderDetail.getItem();
            item.setItemStock(item.getItemStock()+orderDetail.getQuantity());
            itemDao.update(item);
        });
        return orderDao.delete(id);
    }
}
