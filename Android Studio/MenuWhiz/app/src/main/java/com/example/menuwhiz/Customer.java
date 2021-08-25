package com.example.menuwhiz;

//customer object that holds details of customers
public class Customer {

    private String customer_id;
    private String Name;
    private String Email;
    private String Phone;


    Customer( String id, String name, String email, String phone) {
        this.Name=name;
        this.Email= email;
        this.Phone= phone;
        this.customer_id=id;

    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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

}
