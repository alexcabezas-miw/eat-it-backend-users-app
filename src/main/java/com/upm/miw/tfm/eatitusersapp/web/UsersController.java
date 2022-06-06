package com.upm.miw.tfm.eatitusersapp.web;

import com.upm.miw.tfm.eatitusersapp.exception.UnauthorizedOperationValidationException;
import com.upm.miw.tfm.eatitusersapp.exception.ValidationException;
import com.upm.miw.tfm.eatitusersapp.service.UsersService;
import com.upm.miw.tfm.eatitusersapp.service.model.Roles;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserOutputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public static final String GET_USER_BY_USERNAME_PATH = "/";
    public static final String GET_USER_ROLES = "/roles";
    public static final String DELETE_USER_BY_USERNAME_PATH = "";


    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(CREATE_USERS_PATH)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER_CREATOR')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<ListUserDTO>> getUsers() {
        return ResponseEntity.ok().body(this.usersService.getAllUsers());
    }

    @PutMapping(EDIT_ROLES_PATH + "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @GetMapping(GET_USER_BY_USERNAME_PATH + "{username}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEFAULT_USER')")
    public ResponseEntity<ListUserDTO> getUserByUsername(@PathVariable("username") String username) {
        try {
            UserDetails integrationUser = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(!integrationUser.getUsername().equals(username)
                    && !integrationUser.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.name()))) {
                throw new UnauthorizedOperationValidationException();
            }
            ListUserDTO user = this.usersService.findUserByUsername(username);
            return ResponseEntity.ok().body(user);
        } catch (ValidationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(GET_USER_ROLES + "/" + "{username}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEFAULT_USER')")
    public ResponseEntity<Collection<String>> getRolesByUsername(@PathVariable("username") String username) {
        try {
            UserDetails integrationUser = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(!integrationUser.getUsername().equals(username)
                    && !integrationUser.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.name()))) {
                throw new UnauthorizedOperationValidationException();
            }
            return ResponseEntity.ok().body(this.usersService.getRolesByUsername(username));
        } catch (ValidationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(DELETE_USER_BY_USERNAME_PATH + "/" + "{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeUserByUsername(@PathVariable("username") String username) {
        try {
            this.usersService.removeUserByUsername(username);
            return ResponseEntity.noContent().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
