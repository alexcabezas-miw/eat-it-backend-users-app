package com.upm.miw.tfm.eatitusersapp.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class ExternalUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final AuthUserDetailService authUserDetailService;

    public ExternalUserDetailsAuthenticationProvider(AuthUserDetailService authUserDetailService) {
        this.authUserDetailService = authUserDetailService;
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return this.authUserDetailService.loadUserByUsernameAndPassword(username, authentication.getCredentials().toString());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // No additional checks
    }
}
