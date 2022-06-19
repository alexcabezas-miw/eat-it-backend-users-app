package com.upm.miw.tfm.eatitusersapp.web;

import com.upm.miw.tfm.eatitusersapp.exception.ValidationException;
import com.upm.miw.tfm.eatitusersapp.service.shoppingcart.ShoppingCartService;
import com.upm.miw.tfm.eatitusersapp.web.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(ShoppingCartController.SHOPPING_CART_PATH)
public class ShoppingCartController {
    public static final String SHOPPING_CART_PATH = "/shoppingcart";
    public static final String ADD_PRODUCT_TO_SHOPPING_CART = "/add/{barcode}";
    public static final String GET_SHOPPING_CART_PRODUCT_ITEMS = "/items";
    public static final String CLEAN_SHOPPING_CART_ITEMS = "/clean";


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

    @GetMapping(GET_SHOPPING_CART_PRODUCT_ITEMS)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<ProductDTO>> getShoppingCartItems() {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUsername();
            return ResponseEntity.ok().body(this.shoppingCartService.getShoppingCartItems(username));
        } catch (ValidationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(CLEAN_SHOPPING_CART_ITEMS)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cleanShoppingCartItems() {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUsername();
            this.shoppingCartService.cleanShoppingCart(username);
            return ResponseEntity.noContent().build();
        } catch (ValidationException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
