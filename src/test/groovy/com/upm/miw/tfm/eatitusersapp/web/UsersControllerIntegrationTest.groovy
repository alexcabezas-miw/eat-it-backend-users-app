package com.upm.miw.tfm.eatitusersapp.web

import com.upm.miw.tfm.eatitusersapp.AbstractIntegrationTest
import com.upm.miw.tfm.eatitusersapp.MockedProductClient
import com.upm.miw.tfm.eatitusersapp.config.ProductClientFactory
import com.upm.miw.tfm.eatitusersapp.service.model.Roles
import com.upm.miw.tfm.eatitusersapp.service.model.User
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO
import com.upm.miw.tfm.eatitusersapp.web.dto.ProductToleranceInputDTO
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
class UsersControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UsersController usersController

    @SpringBean
    ProductClientFactory productClientFactory = Mock()

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    def "Create a user works successfully when user does not exist and user is admin" () {
        given:
        CreateUserInputDTO userInputDTO = CreateUserInputDTO.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alejandro Cabezas")
                .password("password")
                .build()

        when:
        def response = this.usersController.createUser(userInputDTO)

        then:
        response.getStatusCode() == HttpStatus.CREATED
    }

    @WithMockUser(username = "admin", roles = ["DEFAULT_USER"])
    def "Create a user throw exception when user is default" () {
        given:
        CreateUserInputDTO userInputDTO = CreateUserInputDTO.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alejandro Cabezas")
                .password("password")
                .build()

        when:
        this.usersController.createUser(userInputDTO)

        then:
        thrown(AccessDeniedException)
    }

    @WithMockUser(username = "admin", roles = ["USER_CREATOR"])
    def "Create a user works successfully when user does not exist and user is user creator" () {
        given:
        CreateUserInputDTO userInputDTO = CreateUserInputDTO.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alejandro Cabezas")
                .password("password")
                .build()

        when:
        def response = this.usersController.createUser(userInputDTO)

        then:
        response.getStatusCode() == HttpStatus.CREATED
    }

    def "Create a user throws exception when no authentication is provided" () {
        given:
        CreateUserInputDTO userInputDTO = CreateUserInputDTO.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alejandro Cabezas")
                .password("password")
                .build()

        when:
        this.usersController.createUser(userInputDTO)

        then:
        thrown(Exception)
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    def "Create a user returns 400 when username already exists" () {
        given:
        User user = User.builder().username("username").build()
        this.usersRepository.save(user)

        CreateUserInputDTO userInputDTO = CreateUserInputDTO.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alejandro Cabezas")
                .password("password")
                .build()

        when:
        def response = this.usersController.createUser(userInputDTO)

        then:
        response.getStatusCode() == HttpStatus.BAD_REQUEST
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    def "List all users returns 200 and all the ones stored in the database if user is admin" () {
        given:
        usersRepository.saveAll(List.of(
                User.builder().username("username1").build(),
                User.builder().username("username2").build(),
                User.builder().username("username3").build()
        ))

        when:
        def response = this.usersController.getUsers()

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody().size() == 3
    }

    @WithMockUser(username = "user", roles = ["DEFAULT_USER"])
    def "List all users throws Authentication exception if user is not admin" () {
        given:
        usersRepository.saveAll(List.of(
                User.builder().username("username1").build(),
                User.builder().username("username2").build(),
                User.builder().username("username3").build()
        ))

        when:
        this.usersController.getUsers()

        then:
        thrown(AccessDeniedException)
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    def "edit roles returns 400 if username does not exist" () {
        when:
        def response = this.usersController.editRolesOfUser("not-found", ["ADMIN"])

        then:
        response.getStatusCode() == HttpStatus.BAD_REQUEST
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    def "edit roles returns 400 if roles not exist" () {
        given:
        this.usersRepository.save(User.builder().username("username").build())

        when:
        def response = this.usersController.editRolesOfUser("username", ["NOT_EXIST"])

        then:
        response.getStatusCode() == HttpStatus.BAD_REQUEST
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    def "edit roles returns 204 if roles were changed correctly and user is admin" () {
        given:
        this.usersRepository.save(User.builder().username("username").build())

        when:
        def response = this.usersController.editRolesOfUser("username", ["ROLE_ADMIN"])

        then:
        response.getStatusCode() == HttpStatus.NO_CONTENT
    }

    @WithMockUser(username = "admin", roles = ["DEFAULT_USER"])
    def "edit roles throw exception if user role is not admin" () {
        given:
        this.usersRepository.save(User.builder().username("username").build())

        when:
        this.usersController.editRolesOfUser("username", ["ROLE_ADMIN"])

        then:
        thrown(AccessDeniedException)
    }


    @WithMockUser(username = "not_exist", roles = ["DEFAULT_USER"])
    def "get user by username returns 404 if user does not exist" () {
        expect:
        def response = usersController.getUserByUsername("not_exist")
        response.getStatusCode() == HttpStatus.NOT_FOUND
    }

    @WithMockUser(username = "username", roles = ["DEFAULT_USER"])
    def "get user by username returns 200 and user if user exists" () {
        when:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alex")
                .build()
        )

        then:
        def response = usersController.getUserByUsername("username")
        response.getStatusCode() == HttpStatus.OK
        response.getBody().getName()  == "Alex"
    }

    @WithMockUser(username = "acabezas", roles = ["ADMIN"])
    def "get user by username returns 200 and user if requested user is different from authenticated but user is admin" () {
        when:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alex")
                .build())

        then:
        def response = usersController.getUserByUsername("username")
        response.getStatusCode() == HttpStatus.OK
        response.getBody().getName()  == "Alex"
    }

    @WithMockUser(username = "acabezas", roles = ["DEFAULT_USER"])
    def "get user by username returns 404 if requested user is different from authenticated" () {
        expect:
        def response = usersController.getUserByUsername("username")
        response.getStatusCode() == HttpStatus.NOT_FOUND
    }

    @WithMockUser(username = "acabezas", roles = ["OTHER_ROLE"])
    def "get user by username throw authentication exception if user has no matching roles" () {
        when:
        usersController.getUserByUsername("username")

        then:
        thrown(AccessDeniedException)
    }

    @WithMockUser(username = "username", roles = ["DEFAULT_USER"])
    def "get roles by username returns roles if authenticated user is the same as the requested username" () {
        given:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alex")
                .roles([Roles.ROLE_DEFAULT_USER])
                .build())

        when:
        def roles = usersController.getRolesByUsername("username")

        then:
        roles.getStatusCode() == HttpStatus.OK
        roles.getBody().size() == 1
    }

    @WithMockUser(username = "acabezas", roles = ["ADMIN"])
    def "get roles by username returns roles if authenticated user is admin" () {
        given:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alex")
                .roles([Roles.ROLE_DEFAULT_USER])
                .build())

        when:
        def roles = usersController.getRolesByUsername("username")

        then:
        roles.getStatusCode() == HttpStatus.OK
        roles.getBody().size() == 1
    }

    @WithMockUser(username = "rcabrera", roles = ["DEFAULT_USER"])
    def "get roles by username returns empty if authenticated user is not admin" () {
        given:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alex")
                .roles([Roles.ROLE_DEFAULT_USER])
                .build())

        when:
        def roles = usersController.getRolesByUsername("username")

        then:
        roles.getStatusCode() == HttpStatus.NOT_FOUND
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    def "remove user by username deletes the username and returns 204 if user exists and user is admin" () {
        given:
        usersRepository.save(User.builder()
                .username("username")
                .age("24")
                .gender("Hombre")
                .nationality("España")
                .name("Alex")
                .roles([Roles.ROLE_DEFAULT_USER])
                .build())

        when:
        def response = usersController.removeUserByUsername("username")

        then:
        response.getStatusCode() == HttpStatus.NO_CONTENT
        usersRepository.findAll().isEmpty()
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    def "remove user by username returns 400 if user does not exists and user is admin" () {
        when:
        def response = usersController.removeUserByUsername("username")

        then:
        response.getStatusCode() == HttpStatus.BAD_REQUEST
    }

    @WithMockUser(username = "user", roles = ["DEFAULT_USER"])
    def "remove user by username throws exception if user is not admin" () {
        when:
        usersController.removeUserByUsername("username")

        then:
        thrown(AccessDeniedException)
    }

    @WithMockUser(username = "acabezas", roles = ["DEFAULT_USER"])
    def "user can eat product which ingredients fits his restrictions" () {
        given:
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(["Pechuga de pavo", "Jamón"])
        usersRepository.save(User.builder().username("acabezas").restrictions(["Carne"]).restrictedIngredients(["Cacahuetes"]).build())

        when:
        def response = usersController.userCanEatProduct(new ProductToleranceInputDTO(["Maíz", "Huevo", "Cebolla"]))

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody().isCanEatIt()
        response.getBody().getBlockingIngredients().isEmpty()
    }

    @WithMockUser(username = "acabezas", roles = ["DEFAULT_USER"])
    def "user can not eat product which ingredients ddo not fit his restrictions" () {
        given:
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(["Pechuga de pavo", "Jamón"])
        usersRepository.save(User.builder().username("acabezas").restrictions(["Carne"]).restrictedIngredients(["Cacahuetes"]).build())

        when:
        def response = usersController.userCanEatProduct(new ProductToleranceInputDTO(["Maíz", "Cacahuetes", "Cebolla"]))

        then:
        response.getStatusCode() == HttpStatus.OK
        !response.getBody().isCanEatIt()
        response.getBody().getBlockingIngredients().size() == 1
        response.getBody().getBlockingIngredients().contains("Cacahuetes")
    }

    @WithMockUser(username = "acabezas", roles = ["DEFAULT_USER"])
    def "server returns 404 when user was not found by username" () {
        given:
        productClientFactory.getInstance(_ as String, _ as String) >> new MockedProductClient(["Pechuga de pavo", "Jamón"])

        when:
        def response = usersController.userCanEatProduct(new ProductToleranceInputDTO(["Maíz", "Cacahuetes", "Cebolla"]))

        then:
        response.getStatusCode() == HttpStatus.NOT_FOUND
    }

    def "server throws error when user is not authenticated" () {
        when:
        usersController.userCanEatProduct(new ProductToleranceInputDTO(["Maíz", "Cacahuetes", "Cebolla"]))

        then:
        thrown(AuthenticationCredentialsNotFoundException)
    }
}
