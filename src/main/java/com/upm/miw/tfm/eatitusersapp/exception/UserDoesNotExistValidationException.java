package com.upm.miw.tfm.eatitusersapp.exception;

public class UserDoesNotExistValidationException extends ValidationException {

    public UserDoesNotExistValidationException(String username) {
        super("The user " + username + " does not exist.");
    }
}
