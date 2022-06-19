package com.upm.miw.tfm.eatitusersapp.service.client.product

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.MockedProductClient
import com.upm.miw.tfm.eatitusersapp.config.ProductClientFactory
import com.upm.miw.tfm.eatitusersapp.exception.UnauthorizedOperationValidationException
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
class ProductClientFacadeTest extends AbstractIntegrationTest {

    @Autowired
    ProductClientFacade productClientFacade

    @SpringBean
    ProductClientFactory productClientFactory = Mock()

    def setup() {
        productClientFacade
    }

    @WithMockUser(username = "acabezas", password = "pass")
    def "when product exists, facade existsProduct method returns true"() {
        given:
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(200)

        when:
        boolean exists = productClientFacade.productExists("barcode1")

        then:
        exists
    }

    @WithMockUser(username = "acabezas", password = "pass")
    def "when product does not exist, facade existsProduct method returns false"() {
        given:
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(404)

        when:
        boolean exists = productClientFacade.productExists("barcode1")

        then:
        !exists
    }

    def "when user is not authenticated, method throws exception"() {
        given:
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(200)

        when:
        productClientFacade.productExists("barcode1")

        then:
        thrown(UnauthorizedOperationValidationException)
    }
}
