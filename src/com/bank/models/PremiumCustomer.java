package com.bank.models;

import com.bank.models.enums.CustomerType;
import com.bank.models.enums.Role;

public class PremiumCustomer extends Customer {
    private static final double TRANSACTION_LIMIT = 50000.0;

    public PremiumCustomer(String name, int age, String address, String contact) {
        super(name, age, address, contact, Role.CUSTOMER);
    }

    @Override
    public CustomerType getCustomerType() {
        return CustomerType.PREMIUM;
    }

    @Override
    public double getTransactionLimit() {
        return TRANSACTION_LIMIT;
    }
}
