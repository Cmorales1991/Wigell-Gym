package org.example.wigellgym.repositories;

import org.example.wigellgym.entities.GymCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GymCustomerRepository extends JpaRepository<GymCustomer, Long> {

    Optional<GymCustomer> findByUsername(String user);

}
