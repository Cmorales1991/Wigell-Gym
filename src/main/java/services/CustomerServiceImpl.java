package services;


import entities.Customer;
import exceptions.InvalidCustomerException;
import exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        if (customer.getFirstName() == null || customer.getFirstName().isBlank()) {
            throw new InvalidCustomerException("First name is required");
        }
        if (customer.getLastName() == null || customer.getLastName().isBlank()) {
            throw new InvalidCustomerException("Last name is required");
        }
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new InvalidCustomerException("Email is required");
        }
        // kontroll ifall email redan finns i systemet
        customerRepository.findByEmail(customer.getEmail()).ifPresent(c->{
            throw new InvalidCustomerException("Email already exists:" + customer.getEmail());
        });
        return customerRepository.save(customer);
        }

@Override
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        if (customer.getFirstName() != null || customer.getFirstName().isBlank()) {
            existingCustomer.setFirstName(customer.getFirstName());
        }
        if (customer.getLastName() != null || customer.getLastName().isBlank()) {
            existingCustomer.setLastName(customer.getLastName());
        }
        if (customer.getEmail() != null || customer.getEmail().isBlank()) {
            //kontroll ifall email redan används av någon
            customerRepository.findByEmail(customer.getEmail()).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new InvalidCustomerException("Email already exists:" + customer.getEmail());
                }
            });
            existingCustomer.setEmail(customer.getEmail());
        }
        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        customerRepository.delete(existingCustomer);

    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}

