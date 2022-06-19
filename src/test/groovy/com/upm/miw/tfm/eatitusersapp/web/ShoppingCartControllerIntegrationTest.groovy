package com.upm.miw.tfm.eatitusersapp.web

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.MockedProductClient
import com.upm.miw.tfm.eatitusersapp.config.ProductClientFactory
import com.upm.miw.tfm.eatitusersapp.service.model.ShoppingCart
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
class ShoppingCartControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    ShoppingCartController shoppingCartController

    @SpringBean
    ProductClientFactory productClientFactory = Mock()

    @WithMockUser(username = "acabezas", password = "pass")
    def "add product to user shopping cart returns 200 when user is authenticated and product exists" () {
        given:
        productClientFactory.getInstance("acabezas", "pass") >> new MockedProductClient(200)

        when:
        def response = shoppingCartController.addProductToCart("barcode1")

        then:
        response.getStatusCode() == HttpStatus.OK
        this.shoppingCartRepository.findById("acabezas").isPresent()
        this.shoppingCartRepository.findById("acabezas").get().getProducts().size() == 1
        this.shoppingCartRepository.findById("acabezas").get().getProducts().contains("barcode1")
    }

    @WithMockUser(username = "acabezas", password = "pass")
    def "add product to user shopping cart returns 400 when user is authenticated but product does not exist" () {
        given:
        productClientFactory.getInstance("acabezas", "pass") >> new MockedProductClient(404)

        when:
        def response = shoppingCartController.addProductToCart("barcode1")

        then:
        response.getStatusCode() == HttpStatus.BAD_REQUEST
    }

    def "add product to user shopping cart throws error when user is not authenticated" () {
        when:
        shoppingCartController.addProductToCart("barcode1")

        then:
        thrown(AuthenticationCredentialsNotFoundException)
    }

    @WithMockUser(username = "acabezas")
    def "list item from product cart returns 200 and items when user is authenticated" () {
        given:
        this.shoppingCartRepository.save(ShoppingCart.builder().username("acabezas").products(["barcode1"] as HashSet).build())

        when:
        def response = shoppingCartController.getShoppingCartItems()

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody().size() == 1
    }

    @WithMockUser(username = "acabezas")
    def "list item from product cart returns 404 when shopping cart was not found by username" () {
        when:
        def response = shoppingCartController.getShoppingCartItems()

        then:
        response.getStatusCode() == HttpStatus.NOT_FOUND
    }

    def "list item from product cart throws error user is not authenticated" () {
        when:
        shoppingCartController.getShoppingCartItems()

        then:
        thrown(AuthenticationCredentialsNotFoundException)
    }
}
