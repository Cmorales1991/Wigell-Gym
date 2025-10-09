package org.example.wigellgym.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidGymCustomerException extends RuntimeException {
    public InvalidGymCustomerException(String message) {
        super(message);
    }
}
