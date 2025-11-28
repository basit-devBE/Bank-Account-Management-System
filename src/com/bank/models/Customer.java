package com.bank.models;

import com.bank.models.enums.CustomerType;
import com.bank.models.enums.Role;

public class Customer {
    private String customerId;
    private String name;
    private int age;
    private String address;
    private String contact;
    private CustomerType customerType;
    private Role role;
    private double transactionLimit;
    private static int customerCount = 0;

    public Customer(String name, int age, String address, String contact, Role role, CustomerType customerType) {
        this.customerId = (role == Role.MANAGER ? "MGR" : "CUST") + String.format("%05d", ++customerCount);
        this.name = name;
        this.age = age;
        this.customerType = role == Role.MANAGER ? null : customerType;
        this.role = role;
        this.address = address;
        this.contact = contact;
        this.transactionLimit = role == Role.MANAGER ? Double.MAX_VALUE : 
                               (customerType == CustomerType.PREMIUM ? 50000.0 : 10000.0);
    }

    

    public String getName() {
        return name;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getContact() {
        return contact;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }
    
    public Role getRole() {
        return role;
    }
    
    public boolean isManager() {
        return this.role == Role.MANAGER;
    }
    
    public double getTransactionLimit() {
        return transactionLimit;
    }
    
    @Override
    public String toString() {
        if (isManager()) {
            return "Manager{" +
                    "managerId='" + customerId + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", role=" + role +
                    '}';
        }
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", customerType=" + customerType +
                ", transactionLimit=$" + String.format("%,.2f", transactionLimit) +
                '}';
    }
}
