package org.example.wigellgym.services;


import org.example.wigellgym.entities.GymCustomer;
import org.example.wigellgym.exceptions.InvalidGymCustomerException;
import org.example.wigellgym.exceptions.GymResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.example.wigellgym.repositories.GymCustomerRepository;

import java.util.List;
import java.util.Optional;


@Service
public class GymGymCustomerServiceImpl implements GymCustomerService {

    private final GymCustomerRepository gymCustomerRepository;
    private static final Logger logger = LoggerFactory.getLogger(GymGymCustomerServiceImpl.class.getName());

    public GymGymCustomerServiceImpl(GymCustomerRepository gymCustomerRepository) {
        this.gymCustomerRepository = gymCustomerRepository;
    }

    @Override
    public GymCustomer createCustomer(GymCustomer gymCustomer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' is attempting to create a new customer with username '{}'", username, gymCustomer.getUsername());
        if (gymCustomer.getFirstName() == null || gymCustomer.getFirstName().isBlank()) {
            logger.warn("User '{}' failed to create customer: first name is missing", username);
            throw new InvalidGymCustomerException("First name is required");
        }
        if (gymCustomer.getLastName() == null || gymCustomer.getLastName().isBlank()) {
            logger.warn("User '{}' failed to create customer: last name is missing", username);
            throw new InvalidGymCustomerException("Last name is required");
        }
        if (gymCustomer.getUsername() == null || gymCustomer.getUsername().isBlank()) {
            logger.warn("User '{}' failed to create customer: Username is missing", username);
            throw new InvalidGymCustomerException("Username is required");
        }

        gymCustomerRepository.findByUsername(gymCustomer.getUsername()).ifPresent(c -> {
            logger.warn("User '{}' tried to create a customer with existing username '{}'", username, gymCustomer.getUsername());
            throw new InvalidGymCustomerException("Username already exists: " + gymCustomer.getUsername());
        });

        GymCustomer saved = gymCustomerRepository.save(gymCustomer);
        logger.info("User '{}' successfully created customer with id '{}' and username '{}'", username, saved.getId(), saved.getUsername());
        return saved;
        }

@Override
    public GymCustomer updateCustomer(Long id, GymCustomer gymCustomer) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    logger.info("User '{}' is attempting to update customer with id '{}'", username, id);
    GymCustomer existingGymCustomer = gymCustomerRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("User '{}' failed to update: customer with id '{}' not found", username, id);
                return new GymResourceNotFoundException("Customer", "id", id);
            });

    if (gymCustomer.getFirstName() != null && !gymCustomer.getFirstName().isBlank()) {
        existingGymCustomer.setFirstName(gymCustomer.getFirstName());
    }
    if (gymCustomer.getLastName() != null && !gymCustomer.getLastName().isBlank()) {
        existingGymCustomer.setLastName(gymCustomer.getLastName());
    }
    if (gymCustomer.getUsername() != null && !gymCustomer.getUsername().isBlank()) {
        gymCustomerRepository.findByUsername(gymCustomer.getUsername()).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                logger.warn("User '{}' tried to update customer with id '{}' to an already existing username '{}'", username, id, gymCustomer.getUsername());
                throw new InvalidGymCustomerException("Username already exists: " + gymCustomer.getUsername());
            }
        });
        existingGymCustomer.setUsername(gymCustomer.getUsername());
    }

    GymCustomer updated = gymCustomerRepository.save(existingGymCustomer);
    logger.info("User '{}' successfully updated customer with id '{}'", username, updated.getId());
    return updated;
}

//    @Override
//    public GymCustomer updateCustomerForUser(Long id, GymCustomer gymCustomer, String username) {
//        GymCustomer existingGymCustomer = gymCustomerRepository.findById(id)
//                .orElseThrow(() -> new GymResourceNotFoundException("Customer", "id", id));
//
//        if(!existingGymCustomer.getFirstName().equals(username)) {
//            throw new InvalidGymCustomerException("You can only update your own profile!");
//        }
//
//
//        if (gymCustomer.getFirstName() != null && !gymCustomer.getFirstName().isBlank()) {
//            existingGymCustomer.setFirstName(gymCustomer.getFirstName());
//        }
//        if (gymCustomer.getLastName() != null && !gymCustomer.getLastName().isBlank()) {
//            existingGymCustomer.setLastName(gymCustomer.getLastName());
//        }
//        if (gymCustomer.getEmail() != null && !gymCustomer.getEmail().isBlank()) {
//            //kontroll ifall email redan används av någon
//            gymCustomerRepository.findByEmail(gymCustomer.getEmail()).ifPresent(c -> {
//                if (!c.getId().equals(id)) {
//                    throw new InvalidGymCustomerException("Email already exists:" + gymCustomer.getEmail());
//                }
//            });
//            existingGymCustomer.setEmail(gymCustomer.getEmail());
//        }
//        return gymCustomerRepository.save(existingGymCustomer);
//    }

    @Override
    public void deleteCustomer(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' is attempting to delete customer with id '{}'", username, id);
        GymCustomer existingGymCustomer = gymCustomerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User '{}' failed to delete: customer with id '{}' not found", username, id);
                    return new GymResourceNotFoundException("Customer", "id", id);
                });

        gymCustomerRepository.delete(existingGymCustomer);
        logger.info("User '{}' successfully deleted customer with id '{}'", username, id);
    }

    @Override
    public List<GymCustomer> getAllCustomers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' requested all customers", username);
        List<GymCustomer> customers = gymCustomerRepository.findAll();

        if (customers.isEmpty()) {
            logger.warn("User '{}' found no customers in the system", username);
        } else {
            logger.info("User '{}' retrieved {} customers", username, customers.size());
        }
        return customers;
    }

    @Override
    public Optional<GymCustomer> getCustomerByUsername(String user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' requested customer with username '{}'", username, user);
        Optional<GymCustomer> customer = gymCustomerRepository.findByUsername(user);

        if (customer.isEmpty()) {
            logger.warn("User '{}' could not find customer with username '{}'", username, user);
        } else {
            logger.info("User '{}' retrieved customer with id '{}' and username '{}'", username, customer.get().getId(), user);
        }

        return customer;    }
}

