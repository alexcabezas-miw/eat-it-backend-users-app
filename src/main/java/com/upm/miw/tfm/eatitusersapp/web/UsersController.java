package com.upm.miw.tfm.eatitusersapp.web;

import com.upm.miw.tfm.eatitusersapp.exception.ValidationException;
import com.upm.miw.tfm.eatitusersapp.service.UsersService;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(UsersController.USERS_PATH)
public class UsersController {

    public static final String USERS_PATH = "/users";
    public static final String CREATE_USERS_PATH = "";
    public static final String LIST_USERS_PATH = "";

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(CREATE_USERS_PATH)
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        try {
            CreateUserDTO createdUser = this.usersService.createUser(createUserDTO);
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
}
