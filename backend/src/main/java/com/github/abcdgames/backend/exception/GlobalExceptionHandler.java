package com.github.abcdgames.backend.exception;

import com.github.abcdgames.backend.appuser.AppUserAlreadyExistsException;
import com.github.abcdgames.backend.player.exception.NoSuchPlayerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppUserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAppUserAlreadyExistsException(AppUserAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NoSuchPlayerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchPlayerException(NoSuchPlayerException e) {
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception e) {
        return e.getMessage();
    }
}
