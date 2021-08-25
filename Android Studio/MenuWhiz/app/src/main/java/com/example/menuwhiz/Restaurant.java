package com.example.menuwhiz;

//restaurant object that holds details of restaurants
public class Restaurant {
    private String restaurant_id;
    private String Name;
    private String Email;
    private String Phone;
    private String address;
    private String NFCtag;


    public Restaurant(String restaurant_id, String name, String email, String phone, String address, String NFCtag) {
        this.restaurant_id = restaurant_id;
        Name = name;
        Email = email;
        Phone = phone;
        this.address = address;
        this.NFCtag = NFCtag;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNFCtag() {
        return NFCtag;
    }

    public void setNFCtag(String NFCtag) {
        this.NFCtag = NFCtag;
    }
}
