package com.upm.miw.tfm.eatitusersapp.service.shoppingcart;

import com.upm.miw.tfm.eatitusersapp.exception.ShoppingCartNotFoundValidationException;
import com.upm.miw.tfm.eatitusersapp.exception.UnableToAddProductToCartValidationException;
import com.upm.miw.tfm.eatitusersapp.repository.ShoppingCartRepository;
import com.upm.miw.tfm.eatitusersapp.service.client.product.ProductClientFacade;
import com.upm.miw.tfm.eatitusersapp.service.model.ShoppingCart;
import com.upm.miw.tfm.eatitusersapp.web.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Collection<ProductDTO> getShoppingCartItems(String username) {
        Optional<ShoppingCart> shoppingCart = this.shoppingCartRepository.findById(username);
        return shoppingCart.map(cart -> cart.getProducts().stream()
                        .map(prod -> ProductDTO.builder().barcode(prod).build()).collect(Collectors.toList()))
                .orElseThrow(() -> new ShoppingCartNotFoundValidationException(username));
    }
}
