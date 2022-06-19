package com.upm.miw.tfm.eatitusersapp.service.shoppingcart;

import com.upm.miw.tfm.eatitusersapp.exception.ShoppingCartNotFoundValidationException;
import com.upm.miw.tfm.eatitusersapp.exception.UnableToAddProductToCartValidationException;
import com.upm.miw.tfm.eatitusersapp.repository.ShoppingCartRepository;
import com.upm.miw.tfm.eatitusersapp.service.client.product.ProductClientFacade;
import com.upm.miw.tfm.eatitusersapp.service.model.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductClientFacade productClientFacade;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   ProductClientFacade productClientFacade) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productClientFacade = productClientFacade;
    }

    @Override
    public void addProductToShoppingCart(String username, String productBarcode) {
        Optional<ShoppingCart> shoppingCart = this.shoppingCartRepository.findById(username);
        if(shoppingCart.isEmpty()) {
            shoppingCart = Optional.of(this.shoppingCartRepository.save(ShoppingCart.builder().username(username).build()));
        }

        if(!this.productClientFacade.productExists(productBarcode)) {
            throw new UnableToAddProductToCartValidationException(username, productBarcode);
        }

        shoppingCart.get().getProducts().add(productBarcode);
        this.shoppingCartRepository.save(shoppingCart.get());
    }
}
