package com.github.abcdgames.backend.player.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoSuchPlayerException extends NoSuchElementException {
    public NoSuchPlayerException(String message) {
        super(message);
    }

}
