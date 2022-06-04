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
        listUserDTO.getUsername() == user.getUsername()
    }

    def "conversion from user entity to createUserOutputDTO works correctly" () {
        given:
        User user = User.builder()
                .username("username")
                .id("id")
                .roles([Roles.ROLE_ADMIN, Roles.ROLE_DEFAULT_USER]).build()

        when:
        def userDTO = this.usersMapper.toCreateUserOutputDTO(user)

        then:
        userDTO.getId() == user.getId()
    }
}
