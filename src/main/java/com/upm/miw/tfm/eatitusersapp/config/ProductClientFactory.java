package com.upm.miw.tfm.eatitusersapp.config;

import com.upm.miw.tfm.eatitusersapp.config.properties.ProductClientProperties;
import com.upm.miw.tfm.eatitusersapp.service.client.product.ProductClient;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductClientFactory {

    private final ProductClientProperties productClientProperties;

    public ProductClientFactory(ProductClientProperties productClientProperties) {
        this.productClientProperties = productClientProperties;
    }

    public ProductClient getInstance(String username, String password) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                .target(ProductClient.class, this.productClientProperties.getUrl());
    }

}
