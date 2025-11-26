package com.bank.repository;

import com.bank.models.Customer;

public class CustomerManager {
    private Customer[] customers;
    private int customerCount;

    public CustomerManager() {
        this.customers = new Customer[50];
        this.customerCount = 0;
    }

    public void addCustomer(Customer customer) {
        if (customerCount >= customers.length) {
            resizeArray();
        }
        customers[customerCount++] = customer;
    }


    public void resizeArray() {
        Customer[] newCustomers = new Customer[customers.length * 2];
        System.arraycopy(customers, 0, newCustomers, 0, customers.length);
        customers = newCustomers;
    }

}
