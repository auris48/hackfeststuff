package com.qa.ims.persistence.domain;

import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;

public class Order {
    private Long id;
    private Long customerID;
    private LocalDate orderDate;
    private LocalDate orderDueDate;
    private Map<Item, Integer> orderDetail = new HashMap<>();
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    private double orderCost;
    private CustomerDAO customerDao = new CustomerDAO();

    public Order(Long customer_id, LocalDate orderDate, LocalDate orderDueDate) {
        this.customerID = customer_id;
        this.orderDate = orderDate;
        this.orderDueDate = orderDueDate;

    }


    public Order(Long id, Long customer_id, LocalDate orderDate, LocalDate orderDueDate) {
        this.id = id;
        this.customerID = customer_id;
        this.orderDate = orderDate;
        this.orderDueDate = orderDueDate;
    }

    public Order(Long id, Long customer_id, LocalDate orderDate, LocalDate orderDueDate, Double orderCost) {
        this.id = id;
        this.customerID = customer_id;
        this.orderDate = orderDate;
        this.orderDueDate = orderDueDate;
        this.orderCost = orderCost;
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
        String order_items = "";
        Customer customer = customerDao.read(customerID);
        orderDetailList.stream().mapToDouble(OrderDetail::getOrderDetailCost).sum();
        order_items+=orderDetailList.stream().map(item->item.toString()+"\n").reduce("", String::concat);
        return "id: " + id +
                " order date: " + orderDate +
                " order due date: " + orderDueDate +
                " customer name: " + customer.getFirstName() + " " + customer.getSurname() +
                " customer ID: " + customerID + "\n" +
                order_items +
                "\t\t\t\t\t\t\t\t\t\t\t\ttotal cost:" + orderCost + "\n";


    }

    public Map<Item, Integer> getOrderDetail() {
        return orderDetail;
    }


    public void setOrderDetail(Map<Item, Integer> orderDetail) {
        this.orderDetail.putAll(orderDetail);
    }

    public void addToOrderDetailList(List<OrderDetail> orderDetail) {
        calculateOrderCost();
        this.orderDetailList.addAll(orderDetail);
    }

    public void setOrderDetailList(List<OrderDetail> orderDetail) {
        calculateOrderCost();
        this.orderDetailList=orderDetail;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }

    public void calculateOrderCost() {
        orderDetailList.stream().mapToDouble(OrderDetail::getOrderDetailCost).sum();
    }

    public void printOrderDetails() {
        orderDetailList.forEach(item -> System.out.println(
                        " item id: " + item.getItem().getId() +
                        " item name: " + item.getItem().getItemName() +
                        " item quantity " + item.getQuantity() +
                        " cost: " + item.getOrderDetailCost()));
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public boolean containsItemWithID(Item item){
        return   orderDetailList
                .stream()
                .anyMatch(orderDetail1 ->  orderDetail1.getItem().getId()==item.getId());

    }

    public OrderDetail getExistingOrderDetail(Item item){
        OrderDetail orderDetail=orderDetailList.stream().filter(orderDetail1 -> Objects.equals(orderDetail1.getItem(), item)).findAny().get();
        return orderDetail;
    }
}
