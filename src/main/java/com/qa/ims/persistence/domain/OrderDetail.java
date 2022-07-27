package com.qa.ims.persistence.domain;

public class OrderDetail {

    private Long id;
    private Long orderID;
    private Item item;
    private int quantity;
    private double orderDetailCost;

    public OrderDetail(Long id, Long orderID, Item item, int quantity) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double calculateOrderDetailCost(){
        return item.getItemPrice()*quantity;
    }
}
