package com.upm.miw.tfm.eatitusersapp.service

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.exception.RoleDoesNotExistValidationException
import com.upm.miw.tfm.eatitusersapp.exception.UserAlreadyExistValidationException
import com.upm.miw.tfm.eatitusersapp.exception.UserDoesNotExistValidationException
import com.upm.miw.tfm.eatitusersapp.service.model.Roles
import com.upm.miw.tfm.eatitusersapp.service.model.User
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO
import org.springframework.beans.factory.annotation.Autowired

class UsersServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UsersService usersService

    def "create a user works successfully" () {
        given:
        CreateUserInputDTO dto = CreateUserInputDTO.builder()
                .username("username").build()

        when:
        def savedUser = usersService.createUser(dto)

        then:
        savedUser.getId() != ""
    }

    def "create user throws exception if roles not exist" () {
        given:
        CreateUserInputDTO dto = CreateUserInputDTO.builder()
                .username("username")
                .roles(["NOT_FOUND"]).build()

        when:
        usersService.createUser(dto)

        then:
        thrown(RoleDoesNotExistValidationException)
    }

    def "create an already existing user throws an exception" () {
        given:
        CreateUserInputDTO dto = CreateUserInputDTO.builder()
                .username("username").build()
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

    def "edit roles throws exception when username does not exist in database" () {
        when:
        usersService.editRolesByUsername("NOT_FOUND", Collections.emptyList())

        then:
        thrown(UserDoesNotExistValidationException)
    }


    def "edit roles throws exception when role does not exist" () {
        given:
        usersRepository.save(User.builder().username("FOUND").build())

        when:
        usersService.editRolesByUsername("FOUND", ["ROLE_NOT_EXIST"])

        then:
        thrown(RoleDoesNotExistValidationException)
    }

    def "edit roles works correctly when user was found and roles exist" () {
        given:
        usersRepository.save(User.builder().username("FOUND").build())

        when:
        usersService.editRolesByUsername("FOUND", ["ADMIN"])
        def userOpt = usersRepository.findByUsername("FOUND")

        then:
        userOpt.isPresent()
        userOpt.get().getRoles().contains(Roles.ADMIN)
    }

    def "get user by username throws validation exception when user does not exist" () {
        when:
        usersService.findUserByUsername("NOT_FOUND")

        then:
        thrown(UserDoesNotExistValidationException)
    }

    def "get user by username returns the user if the user exists" () {
        given:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .build()
        )

        when:
        def result = usersService.findUserByUsername("username")

        then:
        noExceptionThrown()
        result.getUsername() == "username"
        result.getId() != ""
    }
}
