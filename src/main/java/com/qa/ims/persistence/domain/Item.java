package com.qa.ims.persistence.domain;



import java.time.LocalDate;
import java.util.Objects;

public class Item extends DomainType{

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

    public Item(String[] data) {
        this.id=Long.parseLong(data[0]);
        this.itemName=data[1];
        this.itemStockDate= LocalDate.parse(data[2]);
        this.itemDescription=data[3];
        this.itemStock=Integer.getInteger(data[4]);
        this.itemPrice=Double.parseDouble(data[5]);
    }

    public Item(Long id, String itemName, LocalDate itemStockDate, String itemDescription, int itemStock, double itemPrice) {
        this.id = id;
        this.itemName = itemName;
        this.itemStockDate = itemStockDate;
        this.itemDescription = itemDescription;
        this.itemStock = itemStock;
        this.itemPrice = itemPrice;
    }

    public Item() {

    }

    public static String[] getAllFields(){
        return new String[]{"id", "Item Name", "Item Stock Date", "Item Description", "Item Stock", "Item Price"};
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
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return itemStock == item.itemStock
                && Double.compare(item.itemPrice, itemPrice) == 0
                && Objects.equals(id, item.id)
                && Objects.equals(itemName, item.itemName)
                && Objects.equals(itemStockDate, item.itemStockDate)
                && Objects.equals(itemDescription, item.itemDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemName, itemStockDate, itemDescription, itemStock, itemPrice);
    }



    @Override
    public String toString() {
        return  id + "," + itemName +
                "," + itemStockDate +
                "," + itemDescription +
                "," + itemStock +
                "," + itemPrice;
    }

    public int getItemStock() {
        return itemStock;
    }

    public void setItemStock(int itemStock) {
        this.itemStock = itemStock;
    }

    @Override
    public String[] getFields() {
        return new String[]{"id", "Item Name", "Item Stock Date", "Item Description", "Item Stock", "Item Price"};
    }
}
