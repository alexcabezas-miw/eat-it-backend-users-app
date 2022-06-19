package com.upm.miw.tfm.eatitusersapp

import com.upm.miw.tfm.eatitusersapp.service.client.product.ProductClient
import feign.Request
import feign.Response

import java.nio.charset.Charset

class MockedProductClient implements ProductClient {

    private int status

    MockedProductClient(int status) {
        this.status = status
    }

    @Override
    Response findByBarcode(String barcode) {
        return Response.builder().status(status).request(Request
                .create(Request.HttpMethod.GET, "", [:], new byte[] {}, Charset.defaultCharset()))
                .build()
    }
}
