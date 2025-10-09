package org.example.wigellgym.repositories;

import org.example.wigellgym.entities.GymInstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GymInstructorRepository extends JpaRepository<GymInstructor, Long> {

    List<GymInstructor> findBySpecialityIgnoreCase(String speciality);

}
