package org.example.wigellgym.services;

import org.example.wigellgym.entities.GymWorkout;
import org.example.wigellgym.exceptions.InvalidGymWorkoutException;
import org.example.wigellgym.exceptions.GymResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.example.wigellgym.repositories.GymWorkoutRepository;

import java.util.List;

@Service
public class GymWorkoutServiceImpl implements GymWorkoutService {

    private final GymWorkoutRepository workoutRepository;
    private final double conversionCourse = 11.06; // dagens euro kurs
    private static final Logger logger = LoggerFactory.getLogger(GymWorkoutServiceImpl.class);

    public GymWorkoutServiceImpl(GymWorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    // valutakonvertering
    private void convertPrice(GymWorkout gymWorkout) {
        gymWorkout.setPriceEURO(gymWorkout.getPriceSEK() / conversionCourse);
    }

    @Override
    public GymWorkout addWorkout(GymWorkout gymWorkout) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("User '{}' is attempting to add new workout: {}",username, gymWorkout.getWorkoutName());
        if (gymWorkout.getWorkoutName() == null || gymWorkout.getWorkoutName().isBlank()) {
            logger.warn("User '{}' has failed to add new workout : name is empty ",username);
            throw new InvalidGymWorkoutException("Workout name is required");
        }
        if (gymWorkout.getType() == null || gymWorkout.getType().isBlank()) {
            logger.warn("User '{}' failed to add new workout : type is missing ", username);
            throw new InvalidGymWorkoutException("Workout type is required");
        }
        if (gymWorkout.getPriceSEK() <= 0) {
            logger.warn("User '{}' failed to add new workout with invalid price: {}", username, gymWorkout.getPriceSEK());
            throw new InvalidGymWorkoutException("Workout price must be higher than 0");
        }
        if (gymWorkout.getMaxParticipants() <= 0) {
            logger.warn("User '{}' failed to add new workout: maxParticipants is missing {}", username, gymWorkout.getMaxParticipants());
            throw new InvalidGymWorkoutException("Workout must allow at least 1 participant");
        }

        logger.info("Converting price SEK to EURO for workout {}", gymWorkout.getWorkoutName());
        convertPrice(gymWorkout);

        GymWorkout savedWorkout = workoutRepository.save(gymWorkout);
        logger.info("User '{}' saved workout with id : {} and workout name: {}", username, savedWorkout.getId(), savedWorkout.getWorkoutName());
        return savedWorkout;
    }

    @Override
    public GymWorkout updateWorkout(Long id, GymWorkout gymWorkout) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        GymWorkout existing = workoutRepository.findById(id)
                .orElseThrow(() -> new GymResourceNotFoundException("Workout", "id", id));

        logger.info("User '{}' is attempting to update workout: {}",username, gymWorkout.getWorkoutName());
        if (gymWorkout.getWorkoutName() != null && !gymWorkout.getWorkoutName().isBlank()) {
            existing.setWorkoutName(gymWorkout.getWorkoutName());
        }
        logger.warn("User '{}' is attempting to update workout: workout name is missing {}",username, gymWorkout.getWorkoutName().isBlank());
        if (gymWorkout.getType() != null && !gymWorkout.getType().isBlank()) {
            existing.setType(gymWorkout.getType());
        }
        logger.warn("User '{}' is attempting to update prise {} : price is missing", username, gymWorkout.getPriceSEK());
        if (gymWorkout.getPriceSEK() > 0) {
            existing.setPriceSEK(gymWorkout.getPriceSEK());
        }
        logger.warn("User '{}' is attempting to update maxParticipants with invalid participants {}",username, gymWorkout.getMaxParticipants());
        if (gymWorkout.getMaxParticipants() > 0) {
            existing.setMaxParticipants(gymWorkout.getMaxParticipants());
        }
        if (gymWorkout.getInstructor() != null) {
            existing.setInstructor(gymWorkout.getInstructor());
        logger.info("User '{}' updated instructor to id: {} name: {}", username, gymWorkout.getInstructor().getId(), gymWorkout.getInstructor().getName());

        }

        convertPrice(existing);
        GymWorkout updated = workoutRepository.save(existing);
        logger.info("User '{}' successfully updated workout with id : {}", username, updated.getId());
        return updated;
    }

    @Override
    public void deleteWorkout(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.warn("User '{}' attempted to delete non-existing workout with id: {}", username, id);
        GymWorkout gymWorkout = workoutRepository.findById(id)
                .orElseThrow(() -> new GymResourceNotFoundException("Workout", "id", id));

        workoutRepository.delete(gymWorkout);
        logger.info("User '{}' successfully deleted workout with id: {}", username, id);
    }

    @Override
    public List<GymWorkout> getAllWorkout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<GymWorkout> gymWorkout = workoutRepository.findAll();
        gymWorkout.forEach(this::convertPrice);
        logger.info("User '{}' retrieved all workouts, total count: {}", username, gymWorkout.size());
        return gymWorkout;
    }

    @Override
    public List<GymWorkout> getWorkoutByInstructor(Long instructorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<GymWorkout> gymWorkout = workoutRepository.findByInstructorId(instructorId);
        if (gymWorkout.isEmpty()) {
            logger.warn("User '{}' requested workouts for instructor with id: {} but none were found", username, instructorId);
        } else {
            logger.info("User '{}' retrieved {} workouts for instructor with id: {}", username, gymWorkout.size(), instructorId);
        }
        gymWorkout.forEach(this::convertPrice);
        return workoutRepository.findByInstructorId(instructorId);
    }

    @Override
    public GymWorkout getWorkoutById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        GymWorkout gymWorkout = workoutRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User '{}' requested non-existing workout with id: {}", username, id);
                    return new GymResourceNotFoundException("Workout", "id", id);
                });
        convertPrice(gymWorkout);
        logger.info("User '{}' retrieved workout with id : {}", username, id);
        return gymWorkout;
    }
}
