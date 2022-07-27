package com.qa.ims.persistence.domain;

import com.qa.ims.persistence.dao.CustomerDAO;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Order {
    private Long id;
    private Long customerID;
    private LocalDate orderDate;
    private LocalDate orderDueDate;
    private Map<Item, Integer> orderDetail=new HashMap<>();
    private List<OrderDetail> orderDetailList;
    private double orderCost;


    public Order(Long customer_id, LocalDate orderDate, LocalDate orderDueDate) {
        this.customerID=customer_id;
        this.orderDate=orderDate;
        this.orderDueDate=orderDueDate;
        this.orderDetailList=new ArrayList<>();

    }


    public Order(Long id, Long customer_id, LocalDate orderDate, LocalDate orderDueDate) {
        this.id=id;
        this.customerID=customer_id;
        this.orderDate=orderDate;
        this.orderDueDate=orderDueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomer_id(Long customer_id) {
        this.customerID = customer_id;
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
        int totalCost=0;
       AtomicReference<String> order_items= new AtomicReference<>("");
       orderDetail.keySet().stream().forEach(item -> order_items.set(" Item name: " + item.getItemName() + " Item price " +
               item.getItemPrice() + " Item quantity "+orderDetail.get(item)
               + " Cost " + (orderDetail.get(item)*item.getItemPrice())));

        for (Item item : orderDetail.keySet()) {
            totalCost+=item.getItemPrice()*orderDetail.get(item);
        }


        return  " id: " + id +
                " order date: "  + orderDate  +
                " order due date:  " + orderDueDate +
                " customer name: "+ "\n"+
                " order_items " +"\n"+
                " total cost:" + totalCost;


    }

    public Map<Item, Integer> getOrderDetail() {
        return orderDetail;
    }


    public void setOrderDetail(Map<Item, Integer> orderDetail) {
        this.orderDetail.putAll(orderDetail);
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

    public void printOrderDetails(){
        orderDetail.forEach((item, quantity) -> System.out.println(
                " item id: " + item.getId() +
                " item name: " + item.getItemName() +
                " item quantity " + quantity +
                " cost: " + (item.getItemPrice() * quantity) + "\n"));
    }
}
