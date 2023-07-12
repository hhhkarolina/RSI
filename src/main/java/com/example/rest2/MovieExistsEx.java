package com.example.rest2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class MovieExistsEx extends RuntimeException {
    public MovieExistsEx() {
        super("Podany film juz istnieje!");
    }
}
