package org.example.wigellgym.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidGymWorkoutException extends RuntimeException {
    public InvalidGymWorkoutException(String message) {
        super(message);
    }
}
