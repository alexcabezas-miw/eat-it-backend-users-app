package com.upm.miw.tfm.eatitusersapp.repository

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.service.model.User

class UsersRepositoryIntegrationTest extends AbstractIntegrationTest {

    def "if no user was saved with given username, then findByUsername returns empty" () {
        given:
        def username = "NOT_FOUND"

        when:
        def user = usersRepository.findByUsername(username)

        then:
        user.isEmpty()
    }

    def "if user was saved with given username, then findByUsername returns the user" () {
        given:
        def username = "FOUND"
        User user = User.builder()
                .username(username)
                .build()
        usersRepository.save(user)

        when:
        def foundUser = usersRepository.findByUsername(username)

        then:
        foundUser.isPresent()
        foundUser.get().getId() != ""
        foundUser.get().getUsername() == username
    }
}
