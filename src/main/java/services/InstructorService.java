package services;

import entities.Instructor;

import java.util.List;
import java.util.Optional;

public interface InstructorService {

    Instructor addInstructor(Instructor instructor);
    Instructor updateInstructor(Long id, Instructor instructor);
    void deleteInstructor(Long id);
    List<Instructor> getAllInstructors();
    Optional<Instructor> getInstructorById(Long id);
    List<Instructor> getInstructorsBySpeciality(String speciality);
}
