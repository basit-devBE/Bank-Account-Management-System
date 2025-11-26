package com.bank.models;

import com.bank.models.enums.CustomerType;

public class Customer {
    private String customerId;
    private  String name;
    private String email;
    private CustomerType customerType;
    private static int customerCount = 0;

    public Customer(String name, String email, CustomerType customerType) {
        this.customerId = "CUST" + (++customerCount);
        this.name = name;
        this.email = email;
        this.customerType = customerType;
    }
}
