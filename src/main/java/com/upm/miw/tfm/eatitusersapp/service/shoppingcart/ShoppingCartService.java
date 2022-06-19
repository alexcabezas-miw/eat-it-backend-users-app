package com.upm.miw.tfm.eatitusersapp.service.shoppingcart;

import com.upm.miw.tfm.eatitusersapp.web.dto.ProductDTO;

import java.util.Collection;

public interface ShoppingCartService {
    void addProductToShoppingCart(String username, String productBarcode);
    Collection<ProductDTO> getShoppingCartItems(String username);
}
