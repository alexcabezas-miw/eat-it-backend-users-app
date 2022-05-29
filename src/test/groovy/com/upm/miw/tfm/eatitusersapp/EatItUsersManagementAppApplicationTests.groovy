package com.upm.miw.tfm.eatitusersapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class EatItUsersManagementAppApplicationTests extends AbstractIntegrationTest {

    @Autowired
    ApplicationContext context

    def "application starts correctly" () {
        assert context != null
    }
}
