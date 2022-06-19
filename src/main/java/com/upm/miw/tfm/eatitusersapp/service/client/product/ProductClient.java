package com.upm.miw.tfm.eatitusersapp.service.client.product;

import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface ProductClient {
    @RequestLine("GET /products/barcode/{barcode}")
    Response findByBarcode(@Param("barcode") String barcode);
}
