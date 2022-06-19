package com.upm.miw.tfm.eatitusersapp.service.shoppingcart

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.MockedProductClient
import com.upm.miw.tfm.eatitusersapp.config.ProductClientFactory
import com.upm.miw.tfm.eatitusersapp.exception.ShoppingCartNotFoundValidationException
import com.upm.miw.tfm.eatitusersapp.exception.UnableToAddProductToCartValidationException
import com.upm.miw.tfm.eatitusersapp.exception.UnauthorizedOperationValidationException
import com.upm.miw.tfm.eatitusersapp.service.model.ShoppingCart
import com.upm.miw.tfm.eatitusersapp.web.dto.ProductDTO
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
class ShoppingCartServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    ShoppingCartService shoppingCartService

    @SpringBean
    ProductClientFactory productClientFactory = Mock()

    @WithMockUser(username = "acabezas", password = "pass")
    def "Add product to cart works successfully when user has shopping cart and existing product" () {
        given:
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(200)

        when:
        shoppingCartService.addProductToShoppingCart("acabezas", "barcode1")

        then:
        shoppingCartRepository.findById("acabezas").isPresent()
        shoppingCartRepository.findById("acabezas").get().getProducts().size() == 1
        shoppingCartRepository.findById("acabezas").get().getProducts().contains("barcode1")
    }

    @WithMockUser(username = "acabezas", password = "pass")
    def "Add product to cart throws exception when product was not found" () {
        given:
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(404)

        when:
        shoppingCartService.addProductToShoppingCart("acabezas", "barcode1")

        then:
        thrown(UnableToAddProductToCartValidationException)
    }

    @WithMockUser(username = "acabezas", password = "pass")
    def "Add product to cart works successfully and does not add duplicate product to cart" () {
        given:
        this.shoppingCartRepository.save(ShoppingCart.builder().username("acabezas").products(["barcode1"] as Set<String>).build())
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(200)

        when:
        shoppingCartService.addProductToShoppingCart("acabezas", "barcode1")

        then:
        shoppingCartRepository.findById("acabezas").isPresent()
        shoppingCartRepository.findById("acabezas").get().getProducts().size() == 1
        shoppingCartRepository.findById("acabezas").get().getProducts().contains("barcode1")
    }

    def "Add product to cart throws exception when user is not authenticated" () {
        when:
        shoppingCartService.addProductToShoppingCart("acabezas", "barcode1")

        then:
        thrown(UnauthorizedOperationValidationException)
    }

    def "list items from product cart returns all items if shoppingcart exists" () {
        given:
        this.shoppingCartRepository.save(ShoppingCart.builder().username("acabezas").products(["barcode1"] as HashSet).build())

        when:
        Collection<ProductDTO> products = shoppingCartService.getShoppingCartItems("acabezas")

        then:
        products.size() == 1
    }

    def "list items throws error when no shopping cart was found by username" () {
        when:
        shoppingCartService.getShoppingCartItems("username")

        then:
        thrown(ShoppingCartNotFoundValidationException)
    }
}
