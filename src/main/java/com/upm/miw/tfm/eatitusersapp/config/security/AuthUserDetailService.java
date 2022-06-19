package com.upm.miw.tfm.eatitusersapp.config.security;

import com.upm.miw.tfm.eatitusersapp.service.users.UsersService;
import com.upm.miw.tfm.eatitusersapp.service.client.auth.AuthenticationClient;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthUserDetailService {

    private final AuthenticationClient authenticationClient;
    private final UsersService usersService;

    public AuthUserDetailService(AuthenticationClient authenticationClient,
                                 UsersService usersService) {
        this.authenticationClient = authenticationClient;
        this.usersService = usersService;
    }

    public UserDetails loadUserByUsernameAndPassword(String username, String password) {
        UserCredentialsDTO credentials = UserCredentialsDTO.builder()
                .username(username)
                .password(password).build();

        boolean authenticated = this.authenticationClient.authenticate(credentials);
        if(!authenticated) {
            throw new BadCredentialsException("Bad credentials");
        }

        List<String> roles = new ArrayList<>(usersService.getRolesByUsername(username));
        return IntegrationUser.builder()
                .username(username)
                .password(password)
                .roles(roles).build();
    }
}
