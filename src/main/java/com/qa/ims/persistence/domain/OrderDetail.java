package com.qa.ims.persistence.domain;

import java.util.Objects;

public class OrderDetail {

    private Long id;
    private Item item;
    private int quantity;
    private double orderDetailCost;

    public OrderDetail(Long id, Item item, int quantity) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        orderDetailCost=item.getItemPrice()*quantity;
    }

    public OrderDetail(Item item, int quantity) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        orderDetailCost=item.getItemPrice()*quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public double getOrderDetailCost() {
        return orderDetailCost;
    }

 /*   @Override
    public String toString() {
        return  "\t\t\t\titem name:" + item.getItemName() +
                " item id: " + item.getId() +
                " quantity: " + quantity +
                " cost: " + orderDetailCost;
    }
*/

    @Override
    public String toString() {
        return
                "[item id: " + item.getId() +
                "item name:" + item.getItemName()+
                "item price: " + item.getItemName()+
                "item quantity: "       + quantity +
                "cost: " + orderDetailCost +
                "]";

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public double calculateOrderDetailCost(){
        return item.getItemPrice()*quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetail)) return false;
        OrderDetail that = (OrderDetail) o;
        return quantity == that.quantity && Double.compare(that.orderDetailCost, orderDetailCost) == 0 && Objects.equals(id, that.id) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, quantity, orderDetailCost);
    }
}
