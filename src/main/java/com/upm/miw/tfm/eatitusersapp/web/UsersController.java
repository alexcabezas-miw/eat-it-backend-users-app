package com.upm.miw.tfm.eatitusersapp.web;

import com.upm.miw.tfm.eatitusersapp.exception.ValidationException;
import com.upm.miw.tfm.eatitusersapp.service.UsersService;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserOutputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(UsersController.USERS_PATH)
public class UsersController {

    public static final String USERS_PATH = "/users";
    public static final String CREATE_USERS_PATH = "";
    public static final String LIST_USERS_PATH = "";
    public static final String EDIT_ROLES_PATH = "/roles";


    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(CREATE_USERS_PATH)
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserInputDTO createUserInputDTO) {
        try {
            CreateUserOutputDTO createdUser = this.usersService.createUser(createUserInputDTO);
            return ResponseEntity
                    .created(URI.create(USERS_PATH + "/" + createdUser.getId()))
                    .build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping(LIST_USERS_PATH)
    public ResponseEntity<Collection<ListUserDTO>> getUsers() {
        return ResponseEntity.ok().body(this.usersService.getAllUsers());
    }

    @PutMapping(EDIT_ROLES_PATH + "/{username}")
    public ResponseEntity<?> editRolesOfUser(@PathVariable("username") String username,
                                             @RequestBody Collection<String> roles) {
        try {
            this.usersService.editRolesByUsername(username, roles);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}
