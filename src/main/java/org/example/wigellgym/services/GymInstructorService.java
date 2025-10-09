package org.example.wigellgym.services;

import org.example.wigellgym.entities.GymInstructor;

import java.util.List;
import java.util.Optional;

public interface GymInstructorService {

    GymInstructor addInstructor(GymInstructor gymInstructor);
    GymInstructor updateInstructor(Long id, GymInstructor gymInstructor);
    void deleteInstructor(Long id);
    List<GymInstructor> getAllInstructors();
    Optional<GymInstructor> getInstructorById(Long id);
    List<GymInstructor> getInstructorsBySpeciality(String speciality);
}
