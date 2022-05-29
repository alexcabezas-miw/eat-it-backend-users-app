package com.upm.miw.tfm.eatitusersapp.exception;

public class RoleDoesNotExistValidationException extends ValidationException {

    public RoleDoesNotExistValidationException(String role) {
        super("The role " + role + " is not a valid one.");
    }
}
