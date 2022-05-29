package com.upm.miw.tfm.eatitusersapp.service.mapper

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.service.model.User
import com.upm.miw.tfm.eatitusersapp.web.dto.UserDTO
import org.springframework.beans.factory.annotation.Autowired

class UsersMapperIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UsersMapper usersMapper

    def "conversion from entity to dto works correctly" () {
        given:
        User user = User.builder()
                .username("username")
                .id("id").build()

        when:
        def dto = usersMapper.toDTO(user)

        then:
        dto.getId() == user.getId()
        dto.getUsername() == user.getUsername()
    }


    def "conversion from dto to entity works correctly" () {
        given:
        UserDTO userDTO = UserDTO.builder()
                .username("username")
                .id("id").build()

        when:
        def entity = usersMapper.toEntity(userDTO)

        then:
        entity.getId() == userDTO.getId()
        entity.getUsername() == userDTO.getUsername()
    }
}
