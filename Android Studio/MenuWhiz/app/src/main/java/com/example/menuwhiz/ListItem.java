package com.example.menuwhiz;

//Basically an item object that holds the details about each item
public class ListItem {

    private String item_id;
    private String name;
    private String quantity;
    private String price;
    private String restaurant;

    public ListItem(String item_id, String name, String quantity, String price, String restaurant) {
        this.item_id = item_id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.restaurant = restaurant;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
}
