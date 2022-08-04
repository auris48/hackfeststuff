package com.qa.ims.persistence.domain;

import java.util.List;
import java.util.Objects;

public class Delivery {
    private Long id;
    private Driver driver;
    private List<Order> orders;

    public Delivery() {

    }

    public Delivery(Long id, List<Order> orders, Driver driver) {
        this.id = id;
        this.orders = orders;
        this.driver = driver;
    }

    public Delivery(List<Order> orders, Driver driver) {
        this.orders = orders;
        this.driver = driver;
    }

    public Delivery(DeliveryDetail deliveryDetail) {
        this.orders = orders;
    }

    public Delivery(Long id, Driver driver) {
        this.id=id;
        this.driver=driver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> addToOrderList(List<Order> orders) {
        this.orders.addAll(orders);
        return orders;
    }

    public List<Order> addToOrderList(Order order) {
        this.orders.add(order);
        return orders;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public boolean containsOrderWithID(Order order){
        return   orders.stream()
                .anyMatch(order1 ->  order1.getId()==order.getId());

    }
}
