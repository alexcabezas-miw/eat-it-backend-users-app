package com.upm.miw.tfm.eatitusersapp.exception;

public class UnableToAddProductToCartValidationException extends ValidationException {

    public UnableToAddProductToCartValidationException(String username, String barcode) {
        super("Unable to add product with barcode " + barcode + " to shopping cart of user " + username);
    }
}
