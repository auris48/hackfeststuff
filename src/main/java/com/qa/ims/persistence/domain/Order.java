package com.qa.ims.persistence.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Order {
    private Long id;
    private Long customer_id;
    private LocalDate orderDate;
    private LocalDate orderDueDate;
    private Map<Item, Integer> orderDetail;
    private double orderCost;

    public Order(Long customer_id, LocalDate orderDate, LocalDate orderDueDate) {
        this.customer_id=customer_id;
        this.orderDate=orderDate;
        this.orderDueDate=orderDueDate;
    }


    public Order(Long id, Long customer_id, LocalDate orderDate, LocalDate orderDueDate) {
        this.id=id;
        this.customer_id=customer_id;
        this.orderDate=orderDate;
        this.orderDueDate=orderDueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getOrderDueDate() {
        return orderDueDate;
    }

    public void setOrderDueDate(LocalDate orderDueDate) {
        this.orderDueDate = orderDueDate;
    }

    public String toString() {
        return " id: " + id +
                " order date: "  + orderDate  +
                " order due date:  " + orderDueDate +
                " customer name: " + "placeholder";

    }

    public Map<Item, Integer> getOrderDetail() {
        return orderDetail;
    }


    public void setOrderDetail(Map<Item, Integer> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }

    public void calculateOrderCost() {
        orderDetail.keySet().stream().forEach(item -> orderCost+=item.getItemPrice()*orderDetail.get(item));
        this.orderCost = orderCost;
    }
}
