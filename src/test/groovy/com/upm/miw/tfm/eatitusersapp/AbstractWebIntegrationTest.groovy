package com.upm.miw.tfm.eatitusersapp

import com.upm.miw.tfm.eatitusersapp.service.UsersService
import com.upm.miw.tfm.eatitusersapp.web.UsersController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.test.web.reactive.server.WebTestClient

class AbstractWebIntegrationTest extends AbstractIntegrationTest {

    protected WebTestClient webTestClient

    @Autowired
    private UsersService usersService

    @Autowired
    private ServerProperties serverProperties

    def setup() {
        webTestClient = WebTestClient
                .bindToController(new UsersController(usersService))
                .build()
    }

}
