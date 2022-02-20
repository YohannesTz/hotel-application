package service;

import exceptions.EmailTakenException;
import model.Customer;

import java.util.Collection;
import java.util.HashMap;

public class CustomerService {

    private static final CustomerService CUSTOMER_SERVICE_SINGLTON = new CustomerService();
    private final HashMap<String, Customer> customersMap = new HashMap<>();
    public static CustomerService getCustomerServiceSinglton() {
        return CUSTOMER_SERVICE_SINGLTON;
    }

    private CustomerService() {
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        customersMap.put(email, newCustomer);
    }

    public Customer getCustomer(String customerEmail) {
        return customersMap.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customersMap.values();
    }
}
