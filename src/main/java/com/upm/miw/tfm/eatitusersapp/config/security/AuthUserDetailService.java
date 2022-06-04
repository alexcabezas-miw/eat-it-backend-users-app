package com.upm.miw.tfm.eatitusersapp.config.security;

import com.upm.miw.tfm.eatitusersapp.service.UsersService;
import com.upm.miw.tfm.eatitusersapp.service.auth.AuthenticationClient;
import com.upm.miw.tfm.eatitusersapp.service.model.Roles;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

        Collection<Roles> roles = usersService.getRolesByUsername(username);
        List<String> rolesString = roles.stream().map(Roles::name).collect(Collectors.toList());


        return IntegrationUser.builder()
                .username(username)
                .password(password)
                .roles(rolesString).build();
    }
}
