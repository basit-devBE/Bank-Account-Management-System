package models;

import models.enums.CustomerType;
import models.enums.Role;

public class ManagerCustomer extends Customer {

    public ManagerCustomer(String name, int age, String address, String contact) {
        super(name, age, address, contact, Role.MANAGER);
    }

    @Override
    public CustomerType getCustomerType() {
        return null; // Managers don't have a customer type
    }

    @Override
    public double getTransactionLimit() {
        return Double.MAX_VALUE; // Managers have unlimited transaction limit
    }
}
