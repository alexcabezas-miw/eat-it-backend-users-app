package com.upm.miw.tfm.eatitusersapp.service.client.product;

import com.upm.miw.tfm.eatitusersapp.service.client.product.model.Restriction;
import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface ProductClient {
    @RequestLine("GET /products/barcode/{barcode}")
    Response findByBarcode(@Param("barcode") String barcode);

    @RequestLine("GET /restrictions/{restriction}")
    Restriction getRestrictionDetails(@Param("restriction") String restriction);

}
