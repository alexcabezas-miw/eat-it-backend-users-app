package com.upm.miw.tfm.eatitusersapp.service

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.exception.UserAlreadyExistValidationException
import com.upm.miw.tfm.eatitusersapp.web.dto.UserDTO
import org.springframework.beans.factory.annotation.Autowired

class UsersServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UsersService usersService

    def "create a user works successfully" () {
        given:
        UserDTO dto = UserDTO.builder().username("username").build()

        when:
        def savedUser = usersService.createUser(dto)

        then:
        savedUser.getId() != ""
    }

    def "create an already existing user throws an exception" () {
        given:
        UserDTO dto = UserDTO.builder().username("username").build()
        usersService.createUser(dto)

        when:
        usersService.createUser(dto)

        then:
        thrown(UserAlreadyExistValidationException)
    }
}
