package com.qa.ims.persistence.domain;

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

    @Override
    public String toString() {
        return  "\t\t\t\titem name:" + item.getItemName() +
                " item id: " + item.getId() +
                " quantity: " + quantity +
                " cost: " + orderDetailCost;
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




}
