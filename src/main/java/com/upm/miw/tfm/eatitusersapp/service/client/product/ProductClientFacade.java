package com.upm.miw.tfm.eatitusersapp.service.client.product;

import com.upm.miw.tfm.eatitusersapp.config.ProductClientFactory;
import com.upm.miw.tfm.eatitusersapp.exception.UnauthorizedOperationValidationException;
import feign.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ProductClientFacade {

    private final ProductClientFactory productClientFactory;

    public ProductClientFacade(ProductClientFactory productClientFactory) {
        this.productClientFactory = productClientFactory;
    }

    public boolean productExists(String barcode) {
        ProductClient productClient = getProductClient(getCredentials());
        Response product = productClient.findByBarcode(barcode);
        boolean exists = product.status() == HttpStatus.OK.value();
        product.close();
        return exists;
    }

    private UserDetails getCredentials() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if(authentication == null) {
            throw new UnauthorizedOperationValidationException();
        }
        return (UserDetails) authentication.getPrincipal();
    }

    private ProductClient getProductClient(UserDetails integrationUser) {
        return getProductClient(integrationUser.getUsername(), integrationUser.getPassword());
    }

    private ProductClient getProductClient(String username, String password) {
        return this.productClientFactory.getInstance(username, password);
    }

}
