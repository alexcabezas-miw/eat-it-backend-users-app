package com.upm.miw.tfm.eatitusersapp

import com.upm.miw.tfm.eatitusersapp.service.client.product.ProductClient
import com.upm.miw.tfm.eatitusersapp.service.client.product.model.Restriction
import feign.Request
import feign.Response

import java.nio.charset.Charset

class MockedProductClient implements ProductClient {

    private int status
    private Restriction restriction

    MockedProductClient(int status) {
        this.status = status
    }

    MockedProductClient(Collection<String> restrictionIngredients) {
        this.restriction = new Restriction(restrictionIngredients)
    }

    @Override
    Response findByBarcode(String barcode) {
        return Response.builder().status(status).request(Request
                .create(Request.HttpMethod.GET, "", [:], new byte[] {}, Charset.defaultCharset()))
                .build()
    }

    @Override
    Restriction getRestrictionDetails(String restriction) {
        return this.restriction
    }
}
