package com.upm.miw.tfm.eatitusersapp.service.auth;

import com.upm.miw.tfm.eatitusersapp.config.security.IntegrationUser;
import com.upm.miw.tfm.eatitusersapp.config.security.UserCredentialsDTO;
import com.upm.miw.tfm.eatitusersapp.service.model.User;
import feign.Headers;
import feign.RequestLine;

public interface AuthenticationClient {

    @RequestLine("POST /auth")
    @Headers("Content-Type: application/json")
    IntegrationUser authenticate(UserCredentialsDTO credentialsDTO);
}
