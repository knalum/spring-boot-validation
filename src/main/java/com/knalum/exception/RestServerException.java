package com.knalum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RestServerException extends RuntimeException {
    public RestServerException(String message) {
        super(message);
    }
}
