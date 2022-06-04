package com.upm.miw.tfm.eatitusersapp.exception;

public class UnauthorizedOperationValidationException extends ValidationException {

    public UnauthorizedOperationValidationException() {
        super("Authorization validation");
    }
}
