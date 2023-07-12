package com.example.rest2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MovieNotFoundEx extends RuntimeException {
    public MovieNotFoundEx() {
        super("Nie znaleziono takiego filmu");
    }
}
