package com.upm.miw.tfm.eatitusersapp.service;

import com.upm.miw.tfm.eatitusersapp.service.model.Roles;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserOutputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;

import java.util.Collection;

public interface UsersService {
    CreateUserOutputDTO createUser(CreateUserInputDTO user);
    Collection<ListUserDTO> getAllUsers();
    void editRolesByUsername(String username, Collection<String> roles);
    ListUserDTO findUserByUsername(String username);
    Collection<Roles> getRolesByUsername(String username);
}
