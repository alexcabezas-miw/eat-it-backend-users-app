package com.upm.miw.tfm.eatitusersapp.exception;

public class UserAlreadyExistValidationException extends ValidationException {

    public UserAlreadyExistValidationException(String username) {
        super("User " + username + " already exists!");
    }
}
