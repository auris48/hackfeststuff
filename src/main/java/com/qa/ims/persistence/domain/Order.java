package com.qa.ims.persistence.domain;

import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Order {
    private Long id;
    private Long customerID;
    private LocalDate orderDate;
    private LocalDate orderDueDate;

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
        calculateOrderCost();
        order_items+=orderDetailList.stream().map(item->item.toString()+"\n").reduce("", String::concat);
        return "id: " + id +
                " order date: " + orderDate +
                " order due date: " + orderDueDate +
                " customer name: " + customer.getFirstName() + " " + customer.getSurname() +
                " customer ID: " + customerID + "\n" +
                order_items +
                "total cost:" + orderCost + "\n";


    }

    public List<OrderDetail> addToOrderDetailList(
            List<OrderDetail> orderDetail) {
        this.orderDetailList.addAll(orderDetail);
        return orderDetailList;
    }

    public List<OrderDetail> addToOrderDetailList(OrderDetail orderDetail) {
        this.orderDetailList.add(orderDetail);
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetail) {
        this.orderDetailList=orderDetail;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }

    public void calculateOrderCost() {
        orderCost=orderDetailList.stream().mapToDouble(OrderDetail::getOrderDetailCost).sum();
    }

    public void printOrderDetails() {
        orderDetailList.forEach(item -> System.out.println(item.toString()));
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
        OrderDetail orderDetail=orderDetailList.stream().filter(orderDetail1 ->
                orderDetail1.getItem().getId()==item.getId()).findAny().get();
        return orderDetail;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;

        if (Objects.isNull(orderDetailList)){
            return Double.compare(order.orderCost, orderCost)
                    == 0 && Objects.equals(id, order.id)
                    && Objects.equals(customerID, order.customerID)
                    && Objects.equals(orderDate, order.orderDate)
                    && Objects.equals(orderDueDate, order.orderDueDate)
                    && Objects.equals(orderDetailList, order.orderDetailList);
        } else {
            return Double.compare(order.orderCost, orderCost) == 0
                    && Objects.equals(id, order.id)
                    && Objects.equals(customerID, order.customerID)
                    && Objects.equals(orderDate, order.orderDate)
                    && Objects.equals(orderDueDate, order.orderDueDate)
                    && orderDetailList.equals(order.getOrderDetailList());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerID, orderDate, orderDueDate, orderDetailList, orderCost);
    }
}
