package com.upm.miw.tfm.eatitusersapp.util

import com.upm.miw.tfm.eatitusersapp.config.security.IntegrationUser
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContext

class SecurityContextStub implements SecurityContext {

    Authentication authentication

    SecurityContextStub(String username) {
        this.authentication = new AuthenticationStub(username)
    }

    @Override
    Authentication getAuthentication() {
        return authentication
    }

    @Override
    void setAuthentication(Authentication authentication) {

    }
}

class AuthenticationStub implements Authentication {

    String username

    AuthenticationStub(String username) {
        this.username = username
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return null
    }

    @Override
    Object getCredentials() {
        return null
    }

    @Override
    Object getDetails() {
        return null
    }

    @Override
    Object getPrincipal() {
        IntegrationUser user = new IntegrationUser()
        user.username = username
        user.password = "password"
        return user
    }

    @Override
    boolean isAuthenticated() {
        return false
    }

    @Override
    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    String getName() {
        return null
    }
}
