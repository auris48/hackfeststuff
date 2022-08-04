package com.qa.ims.persistence.domain;

import java.util.List;
import java.util.Objects;

public class DeliveryDetail {
    private Long id;
    private List<Order> orders;

    public DeliveryDetail() {
    }

    public DeliveryDetail(Long id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }

    public DeliveryDetail(List<Order> orders) {
        this.orders = orders;
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

    public List<Order> addToOrderDetailList(List<Order> orders) {
        this.orders.addAll(orders);
        return orders;
    }

    public List<Order> addToOrderDetailList(Order order) {
        this.orders.add(order);
        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryDetail)) return false;
        DeliveryDetail that = (DeliveryDetail) o;
        return Objects.equals(id, that.id) && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orders);
    }

    @Override
    public String toString() {
        return "DeliveryDetail{" +
                "id=" + id +
                ", orders=" + orders +
                '}';
    }

}
