package com.upm.miw.tfm.eatitusersapp.web;

import com.upm.miw.tfm.eatitusersapp.service.UsersService;
import com.upm.miw.tfm.eatitusersapp.web.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(UsersController.USERS_PATH)
public class UsersController {

    public static final String USERS_PATH = "/users";
    public static final String CREATE_USERS_PATH = "";

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(CREATE_USERS_PATH)
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = this.usersService.createUser(userDTO);
        return ResponseEntity
                .created(URI.create(USERS_PATH + "/" + createdUser.getId()))
                .build();
    }
}
