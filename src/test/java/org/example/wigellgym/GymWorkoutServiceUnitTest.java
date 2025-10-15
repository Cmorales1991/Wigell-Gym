package org.example.wigellgym;

import org.example.wigellgym.dto.GymWorkoutDTO;
import org.example.wigellgym.entities.GymWorkout;
import org.example.wigellgym.exceptions.InvalidGymWorkoutException;
import org.example.wigellgym.repositories.GymWorkoutRepository;
import org.example.wigellgym.services.GymWorkoutService;
import org.example.wigellgym.services.GymWorkoutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymWorkoutServiceUnitTest {

    @Mock
    private GymWorkoutRepository gymWorkoutRepository;

    @InjectMocks
    private GymWorkoutServiceImpl gymWorkoutService;

    @Test
    void listWorkoutsShouldReturnTwoWorkouts() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("joey");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<GymWorkout> Workout = new ArrayList<>();
        Workout.add(new GymWorkout(null,"Circle training","Lifting",60,10,200,null));
        Workout.add(new GymWorkout(null,"Body pump","Lifting",60,10,300,null));
        when(gymWorkoutRepository.findAll()).thenReturn(Workout);

        List<GymWorkout> result = gymWorkoutService.getAllWorkout();

        assertEquals(2, result.size());
        assertEquals("Circle training", result.get(0).getWorkoutName());
        assertEquals("Body pump", result.get(1).getWorkoutName());
    }

    @Test
    void addWorkoutShouldThrowWhenNameIsMissing() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("joey");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        GymWorkout invalidWorkout = new GymWorkout(null,"","Leg training",60,10,200,null);

        Exception ex = assertThrows(InvalidGymWorkoutException.class,
                () -> gymWorkoutService.addWorkout(invalidWorkout));

        assertTrue(ex.getMessage().contains("Workout name is required"));
    }
}
