package com.upm.miw.tfm.eatitusersapp.service;

import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;

import java.util.Collection;

public interface UsersService {
    CreateUserDTO createUser(CreateUserDTO user);
    Collection<ListUserDTO> getAllUsers();
}
