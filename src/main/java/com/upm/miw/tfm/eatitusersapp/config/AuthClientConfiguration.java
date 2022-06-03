package com.upm.miw.tfm.eatitusersapp.config;

import com.upm.miw.tfm.eatitusersapp.config.properties.AuthClientProperties;
import com.upm.miw.tfm.eatitusersapp.service.auth.AuthenticationClient;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthClientConfiguration {

    @Bean
    AuthenticationClient authenticationClient(AuthClientProperties properties) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(AuthenticationClient.class, properties.getUrl());
    }
}
