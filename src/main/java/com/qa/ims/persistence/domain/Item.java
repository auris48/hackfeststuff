package com.qa.ims.persistence.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Item {

    private Long id;
    private String itemName;
    private LocalDate itemStockDate;
    private String itemDescription;
    private int itemStock;
    private double itemPrice;

    public Item(String itemName, LocalDate itemStockDate, String itemDescription, int itemStock, double itemPrice) {
        this.itemName = itemName;
        this.itemStockDate = itemStockDate;
        this.itemDescription = itemDescription;
        this.itemStock = itemStock;
        this.itemPrice = itemPrice;
    }



    public Item(Long id, String itemName, LocalDate itemStockDate, String itemDescription, int itemStock, double itemPrice) {
        this.id = id;
        this.itemName = itemName;
        this.itemStockDate = itemStockDate;
        this.itemDescription = itemDescription;
        this.itemStock = itemStock;
        this.itemPrice = itemPrice;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getItemStockDate() {
        return itemStockDate;
    }

    public void setItemStockDate(LocalDate itemStockDate) {
        this.itemStockDate = itemStockDate;
    }
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(item.itemPrice, itemPrice) == 0 && id.equals(item.id) && itemName.equals(item.itemName) && itemStockDate.equals(item.itemStockDate) && itemDescription.equals(item.itemDescription);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Double itemPrice = this.itemPrice;
        Integer itemStock = this.itemStock;
        result = prime * result + ((itemName == null) ? 0 : itemName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((itemDescription == null) ? 0 : itemDescription.hashCode());
        result = prime * result + ((itemStockDate == null) ? 0 : itemStockDate.hashCode());
        result = prime * result + ((itemStockDate == null) ? 0 : itemStock.hashCode());
        result = prime * result + ((itemPrice == null) ? 0 : itemPrice.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return " id: " + id +
                " item name: "  + itemName  +
                " item stock date: " + itemStockDate +
                " item description: " + itemDescription +
                " item stock " + itemStock +
                " item price: " + itemPrice;
    }

    public int getItemStock() {
        return itemStock;
    }

    public void setItemStock(int itemStock) {
        this.itemStock = itemStock;
    }
}
