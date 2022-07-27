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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Takes in Order details for CRUD functionality
 */
public class OrderController implements CrudController<Order> {

    public static final Logger LOGGER = LogManager.getLogger();
    private OrderDAO orderDao;
    private ItemDAO itemDao;
    private Utils utils;

    public OrderController(OrderDAO OrderDao, Utils utils) {
        super();
        this.orderDao = OrderDao;
        this.itemDao=new ItemDAO();
        this.utils = utils;
    }

    @Override
    public List<Order> readAll() {
        List<Order> Orders = orderDao.readAll();
        for (Order Order : Orders) {
            LOGGER.info(Order);
        }
        return Orders;
    }

    @Override
    public Order create() {
        LOGGER.info("Please enter order customer id");
        Long customerID = utils.getLong();
        LOGGER.info("Please enter order due date");
        LocalDate orderDueDate = LocalDate.parse(utils.getString());
        Order order = orderDao.create(new Order(customerID, LocalDate.now(), orderDueDate));
        LOGGER.info("Would you like to add items to order? Yes/No");
        String input = utils.getString();
        if (input.equalsIgnoreCase("Yes")){
            addItemsToOrder(order);
            orderDao.createOrderDetail(order);
            order.calculateOrderCost();
        }
        return order;
    }

    public Order addItemsToOrder(Order order) {
        boolean adding = true;
        Map<Item, Integer> orderDetail = new HashMap<>();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        while (adding) {
            LOGGER.info("Please enter ID of item to add to the order");
            Long id = utils.getLong();
            Item item = itemDao.read(id);
            LOGGER.info("Please enter quantity");
            int quantity = utils.getInt();
            if (item.getItemStock() >= quantity) {
                if (item.getItemStock() == quantity){
                    itemDao.delete(item.getId());
                    orderDetail.put(item, quantity);
                } else{
                    item.setItemStock(item.getItemStock() - quantity);
                    itemDao.update(item);
                    orderDetail.put(item, quantity);
                }
            } else {
                System.out.println("You're trying to order more than the stock");
            }

            LOGGER.info("Would you like to add any more items? Yes/No");
            if (utils.getString().equalsIgnoreCase("No")) {
                adding = false;
            }
        }
        order.setOrderDetail(orderDetail);
        return order;
    }


    @Override
    public Order update() {
        LOGGER.info("Please enter the id of the order you would like to update");
        Long id = utils.getLong();
        LOGGER.info("Please enter customer id");
        Long customerID = utils.getLong();
        LOGGER.info("Please enter order stock date");
        LocalDate orderStockDate = LocalDate.parse(utils.getString());
        LOGGER.info("Please enter order due date");
        LocalDate orderDueDate = LocalDate.parse(utils.getString());;
        Order order = orderDao.update(new Order(id, customerID, orderStockDate, orderDueDate));
        LOGGER.info("Order updated");

        LOGGER.info("Would you like to update items belonging to order? Yes/No");
        if (utils.getString().equalsIgnoreCase("Yes")) {
            System.out.println("Here are all items belonging to this order: ");
            order.printOrderDetails();
        }


        return order;
    }

    @Override
    public int delete() {
        LOGGER.info("Please enter the id of the Order you would like to delete");
        Long id = utils.getLong();
        return orderDao.delete(id);
    }
}
