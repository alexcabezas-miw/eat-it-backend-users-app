package com.upm.miw.tfm.eatitusersapp.service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Document(ShoppingCart.SHOPPING_CART_COLLECTION_NAME)
public class ShoppingCart {
    public static final String SHOPPING_CART_COLLECTION_NAME = "shopping-carts";

    @Id
    private String username;

    @Builder.Default
    private Set<String> products = new HashSet<>();
}
