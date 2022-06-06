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
        usersService.editRolesByUsername("FOUND", ["ROLE_ADMIN"])
        def userOpt = usersRepository.findByUsername("FOUND")

        then:
        userOpt.isPresent()
        userOpt.get().getRoles().contains(Roles.ROLE_ADMIN)
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
    }

    def "get roles returns the roles of the user" () {
        given:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .roles([Roles.ROLE_ADMIN])
                .build()
        )

        when:
        def roles = usersService.getRolesByUsername("username")

        then:
        roles.size() == 1
    }

    def "get roles returns empty if user was not found" () {
        when:
        def roles = usersService.getRolesByUsername("username")

        then:
        roles.isEmpty()
    }

    def "remove user by username removes the user" () {
        given:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .roles([Roles.ROLE_ADMIN])
                .build()
        )

        when:
        usersService.removeUserByUsername("username")

        then:
        usersRepository.findAll().isEmpty()
    }

    def "remove user by username throws exception if user was not found" () {
        when:
        usersService.removeUserByUsername("username")

        then:
        thrown(UserDoesNotExistValidationException)
    }
}
