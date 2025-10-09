package org.example.wigellgym.services;

import org.example.wigellgym.entities.GymInstructor;
import org.example.wigellgym.exceptions.InvalidGymInstructorException;
import org.example.wigellgym.exceptions.GymResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.example.wigellgym.repositories.GymInstructorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GymInstuctorServiceImplGym implements GymInstructorService {

    private final GymInstructorRepository gymInstructorRepository;
    private static final Logger logger = LoggerFactory.getLogger(GymInstuctorServiceImplGym.class);

    public GymInstuctorServiceImplGym(GymInstructorRepository gymInstructorRepository) {
        this.gymInstructorRepository = gymInstructorRepository;
    }

    @Override
    public GymInstructor addInstructor(GymInstructor gymInstructor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' is attempting to add new instructor : {}", username, gymInstructor.getName());
        if (gymInstructor.getName() == null || gymInstructor.getName().isBlank()) {
            logger.warn("User '{}' failed to add instructor: name is empty", username);
            throw new InvalidGymInstructorException("Firstname cant be empty");
        }
        if (gymInstructor.getSpeciality() == null || gymInstructor.getSpeciality().isBlank()) {
            logger.warn("User '{}' failed to add instructor: speciality is empty", username);
            throw new InvalidGymInstructorException("Speciality cant be empty");
        }
        logger.info("User '{}' added instructor with name '{}'", username, gymInstructor.getName());
        return gymInstructorRepository.save(gymInstructor);
    }

    @Override
    public GymInstructor updateInstructor(Long id, GymInstructor gymInstructor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        GymInstructor existingGymInstructor = gymInstructorRepository.findById(id)
                .orElseThrow(()-> new GymResourceNotFoundException("Instructor", "id", id));
            logger.info("User '{}' is attempting to update instructor with id and name : {}, {}", username,gymInstructor.getId(), gymInstructor.getName());
        if (gymInstructor.getName() != null && !gymInstructor.getName().isBlank()) {
            logger.warn("User '{}' failed to update instructor: name is empty", username);
            existingGymInstructor.setName(gymInstructor.getName());
        }
        if (gymInstructor.getSpeciality() != null && !gymInstructor.getSpeciality().isBlank()) {
            logger.warn("User '{}' failed to update instructor: speciality is empty", username);
            existingGymInstructor.setSpeciality(gymInstructor.getSpeciality());
        }
        GymInstructor updatedGymInstructor = gymInstructorRepository.save(existingGymInstructor);
            logger.info("User '{}' updated instructor with id {}", username, updatedGymInstructor.getId());
        return updatedGymInstructor;

    }

    @Override
    public void deleteInstructor(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("User '{}' is attempting to delete instructor with id {}", username, id);
        GymInstructor existingGymInstructor = gymInstructorRepository.findById(id)
                .orElseThrow(()-> new GymResourceNotFoundException("Instructor", "id", id));

        gymInstructorRepository.delete(existingGymInstructor);
        logger.info("User '{}' deleted instructor with id {}", username, id);
    }

    @Override
    public List<GymInstructor> getAllInstructors() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<GymInstructor> gymInstructors = gymInstructorRepository.findAll();
        logger.info("User '{}' retrieved all workouts, total count: {}", username, gymInstructors.size());
        return gymInstructors;
    }

    @Override
    public Optional<GymInstructor> getInstructorById(Long id) {
        return gymInstructorRepository.findById(id);
    }

    @Override
    public List<GymInstructor> getInstructorsBySpeciality(String speciality) {
        return gymInstructorRepository.findBySpecialityIgnoreCase(speciality);
    }
}
