package com.upm.miw.tfm.eatitusersapp.web;

import com.upm.miw.tfm.eatitusersapp.exception.ValidationException;
import com.upm.miw.tfm.eatitusersapp.service.shoppingcart.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ShoppingCartController.SHOPPING_CART_PATH)
public class ShoppingCartController {
    public static final String SHOPPING_CART_PATH = "/shoppingcart";
    public static final String ADD_PRODUCT_TO_SHOPPING_CART = "/add/{barcode}";

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PutMapping(ADD_PRODUCT_TO_SHOPPING_CART)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addProductToCart(@PathVariable("barcode") String productBarcode) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUsername();
            this.shoppingCartService.addProductToShoppingCart(username, productBarcode);
            return ResponseEntity.ok().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
