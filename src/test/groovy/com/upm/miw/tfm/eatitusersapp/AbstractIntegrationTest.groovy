package com.upm.miw.tfm.eatitusersapp

import com.upm.miw.tfm.eatitusersapp.repository.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AbstractIntegrationTest extends Specification {

    @Autowired
    protected UsersRepository usersRepository

    def cleanup() {
        usersRepository.deleteAll()
    }
}
