package com.upm.miw.tfm.eatitusersapp.exception;

public class ShoppingCartNotFoundValidationException extends ValidationException {
    public ShoppingCartNotFoundValidationException(String username) {
        super("Shopping cart for user " + username + " was not found");
    }
}
