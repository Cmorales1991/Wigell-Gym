package services;

import entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(Customer customer);
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerByEmail(String email);
}
