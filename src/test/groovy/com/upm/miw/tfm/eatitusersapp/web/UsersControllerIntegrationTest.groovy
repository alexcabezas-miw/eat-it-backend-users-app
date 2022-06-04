package com.upm.miw.tfm.eatitusersapp.web

import com.upm.miw.tfm.eatitusersapp.AbstractWebIntegrationTest
import com.upm.miw.tfm.eatitusersapp.service.model.User
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.web.reactive.function.BodyInserters

class UsersControllerIntegrationTest extends AbstractWebIntegrationTest {

    def "Create a user works successfully when user does not exist" () {
        expect:
        this.webTestClient.post()
                .uri(UsersController.USERS_PATH + UsersController.CREATE_USERS_PATH)
                .body(BodyInserters.fromValue(CreateUserInputDTO.builder()
                        .username("username")
                        .age("24")
                        .gender("Hombre")
                        .nationality("España")
                        .name("Alejandro Cabezas")
                        .password("password")
                        .build()))
                .exchange()
                .expectStatus().isCreated()
    }

    def "Create a user returns 400 when username already exists" () {
        given:
        User user = User.builder().username("username").build()
        this.usersRepository.save(user)

        expect:
        this.webTestClient.post()
                .uri(UsersController.USERS_PATH + UsersController.CREATE_USERS_PATH)
                .body(BodyInserters.fromValue(CreateUserInputDTO.builder().username("username").build()))
                .exchange()
                .expectStatus().isBadRequest()
    }

    def "Create a user returns 400 when roles does not exist" () {
        given:
        def user = CreateUserInputDTO.builder()
                .username("username")
                .roles(["NOT_FOUND"])
                .build()

        expect:
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

    def "edit roles returns 400 if username does not exist" () {
        expect:
        this.webTestClient
                .put()
                .uri(UsersController.USERS_PATH + UsersController.EDIT_ROLES_PATH + "/not-found")
                .body(BodyInserters.fromValue(["ADMIN"]))
                .exchange()
                .expectStatus().isBadRequest()
    }


    def "edit roles returns 400 if roles not exist" () {
        given:
        this.usersRepository.save(User.builder().username("username").build())

        expect:
        this.webTestClient
                .put()
                .uri(UsersController.USERS_PATH + UsersController.EDIT_ROLES_PATH + "/username")
                .body(BodyInserters.fromValue(["NOT_EXIST"]))
                .exchange()
                .expectStatus().isBadRequest()
    }

    def "edit roles returns 204 if roles were changed correctly" () {
        given:
        this.usersRepository.save(User.builder().username("username").build())

        expect:
        this.webTestClient
                .put()
                .uri(UsersController.USERS_PATH + UsersController.EDIT_ROLES_PATH + "/username")
                .body(BodyInserters.fromValue(["ROLE_ADMIN"]))
                .exchange()
                .expectStatus().isNoContent()
    }

    def "get user by username returns 404 if user does not exist" () {
        expect:
        this.webTestClient
                .get()
                .uri(UsersController.USERS_PATH + UsersController.GET_USER_BY_USERNAME_PATH + "/not_exist")
                .exchange()
                .expectStatus().isNotFound()
    }

    def "get user by username returns 200 and user if user exists" () {
        when:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .build()
        )

        then:
        this.webTestClient
                .get()
                .uri(UsersController.USERS_PATH + UsersController.GET_USER_BY_USERNAME_PATH + "/username")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ListUserDTO.class)
                .value(user -> {
                    user.getUsername() == "username"
                })
    }
}
