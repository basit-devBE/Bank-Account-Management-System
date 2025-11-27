package com.bank.models;

import com.bank.models.enums.CustomerType;

public class Customer {
    private String customerId;
    private  String name;
    private int age;
    private String address;
    private String contact;
    private CustomerType customerType;
    private static int customerCount = 0;

    public Customer(String name, int age, CustomerType customerType, String address, String contact) {
        this.customerId = "CUST" + (++customerCount);
        this.name = name;
        this.age = age;
        this.customerType = customerType;
        this.address = address;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }
}
