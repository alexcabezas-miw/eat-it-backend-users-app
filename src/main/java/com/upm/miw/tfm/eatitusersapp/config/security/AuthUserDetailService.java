package com.upm.miw.tfm.eatitusersapp.config.security;

import com.upm.miw.tfm.eatitusersapp.service.auth.AuthenticationClient;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUserDetailService {

    private final AuthenticationClient authenticationClient;

    public AuthUserDetailService(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
    }

    public UserDetails loadUserByUsernameAndPassword(String username, String password) {
        UserCredentialsDTO credentials = UserCredentialsDTO.builder()
                .username(username)
                .password(password).build();

        IntegrationUser authenticated = this.authenticationClient.authenticate(credentials);
        if(authenticated == null) {
            throw new BadCredentialsException("Bad credentials");
        }
        return authenticated;
    }
}
