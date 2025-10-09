package org.example.wigellgym.services;

import org.example.wigellgym.entities.GymCustomer;

import java.util.List;
import java.util.Optional;

public interface GymCustomerService {

    GymCustomer createCustomer(GymCustomer gymCustomer);
    GymCustomer updateCustomer(Long id, GymCustomer gymCustomer);
//    GymCustomer updateCustomerForUser(Long id, GymCustomer gymCustomer, String username);
    void deleteCustomer(Long id);
    List<GymCustomer> getAllCustomers();
    Optional<GymCustomer> getCustomerByUsername(String user);
}
