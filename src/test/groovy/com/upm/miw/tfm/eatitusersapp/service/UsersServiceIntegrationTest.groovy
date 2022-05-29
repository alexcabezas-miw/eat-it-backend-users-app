package com.upm.miw.tfm.eatitusersapp.service

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.exception.UserAlreadyExistValidationException
import com.upm.miw.tfm.eatitusersapp.service.model.User
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserDTO
import org.springframework.beans.factory.annotation.Autowired

class UsersServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UsersService usersService

    def "create a user works successfully" () {
        given:
        CreateUserDTO dto = CreateUserDTO.builder().username("username").build()

        when:
        def savedUser = usersService.createUser(dto)

        then:
        savedUser.getId() != ""
    }

    def "create an already existing user throws an exception" () {
        given:
        CreateUserDTO dto = CreateUserDTO.builder().username("username").build()
        usersService.createUser(dto)

        when:
        usersService.createUser(dto)

        then:
        thrown(UserAlreadyExistValidationException)
    }

    def "get all users returns all the entries of the database" () {
        given:
        usersRepository.saveAll(List.of(
                User.builder().username("username1").build(),
                User.builder().username("username2").build(),
                User.builder().username("username3").build()
        ))

        when:
        def users = usersService.getAllUsers()

        then:
        !users.isEmpty()
        users.size() == 3
    }
}
