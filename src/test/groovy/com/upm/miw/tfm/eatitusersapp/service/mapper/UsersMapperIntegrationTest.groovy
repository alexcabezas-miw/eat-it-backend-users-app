package com.upm.miw.tfm.eatitusersapp.service.mapper

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.service.model.Roles
import com.upm.miw.tfm.eatitusersapp.service.model.User
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO
import org.springframework.beans.factory.annotation.Autowired

class UsersMapperIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UsersMapper usersMapper

    def "conversion from user entity to listUserDTO works correctly" () {
        given:
        User user = User.builder()
                .username("username")
                .id("id").build()

        when:
        def listUserDTO = usersMapper.toLisUserDTO(user)

        then:
        listUserDTO.getId() == user.getId()
        listUserDTO.getUsername() == user.getUsername()
    }

    def "conversion to entity from CreationDTO maps the roles correctly" () {
        given:
        CreateUserInputDTO userDTO = CreateUserInputDTO.builder()
                .username("username")
                .roles(["ADMIN"]).build()

        when:
        User user = this.usersMapper.fromCreateUserInputDTO(userDTO)

        then:
        user.getRoles() != null
        user.getRoles().contains(Roles.ADMIN)
    }

    def "conversion to ListUserDTO from user entity maps the roles correctly" () {
        given:
        User user = User.builder()
                .username("username")
                .id("id")
                .roles([Roles.ADMIN, Roles.DEFAULT_USER]).build()

        when:
        def userDTO = this.usersMapper.toLisUserDTO(user)

        then:
        userDTO.getRoles() != null
        userDTO.getRoles().containsAll(["ADMIN", "DEFAULT_USER"])
    }

    def "conversion from user entity to createUserOutputDTO works correctly" () {
        given:
        User user = User.builder()
                .username("username")
                .id("id")
                .roles([Roles.ADMIN, Roles.DEFAULT_USER]).build()

        when:
        def userDTO = this.usersMapper.toCreateUserOutputDTO(user)

        then:
        userDTO.getId() == user.getId()
    }
}
