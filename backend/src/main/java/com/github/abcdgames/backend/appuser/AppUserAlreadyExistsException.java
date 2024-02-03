package com.github.abcdgames.backend.appuser;

public class AppUserAlreadyExistsException extends RuntimeException{
    public AppUserAlreadyExistsException(String message) {
        super(message);
    }
}
