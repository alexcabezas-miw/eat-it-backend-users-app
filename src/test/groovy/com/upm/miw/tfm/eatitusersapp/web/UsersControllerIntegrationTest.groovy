package com.upm.miw.tfm.eatitusersapp.web

import com.upm.miw.tfm.eatitusersapp.AbstractWebIntegrationTest
import com.upm.miw.tfm.eatitusersapp.service.model.User
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO
import org.springframework.web.reactive.function.BodyInserters

class UsersControllerIntegrationTest extends AbstractWebIntegrationTest {

    def "Create a user works successfully when user does not exist" () {
        expect:
        this.webTestClient.post()
                .uri(UsersController.USERS_PATH + UsersController.CREATE_USERS_PATH)
                .body(BodyInserters.fromValue(new User(null, "username")))
                .exchange()
                .expectStatus().isCreated()
    }

    def "Create a user returns 400 when username already exists" () {
        expect:
        User user = new User(null, "username")
        this.usersRepository.save(user)
        this.webTestClient.post()
                .uri(UsersController.USERS_PATH + UsersController.CREATE_USERS_PATH)
                .body(BodyInserters.fromValue(user))
                .exchange()
                .expectStatus().isBadRequest()
    }

    def "List all users returns 200 and all the ones stored in the database" () {
        given:
        usersRepository.saveAll(List.of(
                User.builder().username("username1").build(),
                User.builder().username("username2").build(),
                User.builder().username("username3").build()
        ))

        expect:
        this.webTestClient.get()
                .uri(UsersController.USERS_PATH + UsersController.LIST_USERS_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ListUserDTO.class)
                .value(users -> {
                    users.size() == 3
                })
    }
}
