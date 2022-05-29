package com.upm.miw.tfm.eatitusersapp.service.mapper

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.service.model.User
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserDTO
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO
import org.springframework.beans.factory.annotation.Autowired
import spock.util.mop.Use

class UsersMapperIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UsersMapper usersMapper

    def "conversion from entity to creationDTO works correctly" () {
        given:
        User user = User.builder()
                .username("username")
                .id("id").build()

        when:
        def dto = usersMapper.toCreateUserDTO(user)

        then:
        dto.getId() == user.getId()
        dto.getUsername() == user.getUsername()
    }


    def "conversion from creationDTO to entity works correctly" () {
        given:
        CreateUserDTO userDTO = CreateUserDTO.builder()
                .username("username")
                .id("id").build()

        when:
        def entity = usersMapper.fromCreateUserDTO(userDTO)

        then:
        entity.getId() == userDTO.getId()
        entity.getUsername() == userDTO.getUsername()
    }

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
}
