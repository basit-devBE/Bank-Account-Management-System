package com.bank.models;

import com.bank.models.enums.CustomerType;
import com.bank.models.enums.Role;

public class RegularCustomer extends Customer {
    private static final double TRANSACTION_LIMIT = 10000.0;

    public RegularCustomer(String name, int age, String address, String contact) {
        super(name, age, address, contact, Role.CUSTOMER);
    }

    @Override
    public CustomerType getCustomerType() {
        return CustomerType.REGULAR;
    }

    @Override
    public double getTransactionLimit() {
        return TRANSACTION_LIMIT;
    }
}
