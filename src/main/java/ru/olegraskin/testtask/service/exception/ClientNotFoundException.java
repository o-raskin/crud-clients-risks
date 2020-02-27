package ru.olegraskin.testtask.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Number id) {
        super("Client not found with id: " + id);
    }
}
