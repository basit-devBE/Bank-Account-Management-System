package models;

import models.enums.CustomerType;
import models.enums.Role;

public abstract class Customer {
    private String customerId;
    private String name;
    private int age;
    private String address;
    private String contact;
    private Role role;
    private static int customerCount = 0;

    public Customer(String name, int age, String address, String contact, Role role) {
        this.customerId = (role == Role.MANAGER ? "MGR" : "CUST") + String.format("%05d", ++customerCount);
        this.name = name;
        this.age = age;
        this.role = role;
        this.address = address;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public static int getCustomerCount() {
        return customerCount;
    }
    
    public boolean isManager() {
        return this.role == Role.MANAGER;
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract CustomerType getCustomerType();
    public abstract double getTransactionLimit();
    
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
                ", customerType=" + getCustomerType() +
                ", transactionLimit=$" + String.format("%,.2f", getTransactionLimit()) +
                '}';
    }
}
