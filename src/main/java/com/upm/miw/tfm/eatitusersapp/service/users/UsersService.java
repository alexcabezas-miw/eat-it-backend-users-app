package com.upm.miw.tfm.eatitusersapp.service.users;

import com.upm.miw.tfm.eatitusersapp.web.dto.*;

import java.util.Collection;

public interface UsersService {
    CreateUserOutputDTO createUser(CreateUserInputDTO user);
    Collection<ListUserDTO> getAllUsers();
    void editRolesByUsername(String username, Collection<String> roles);
    ListUserDTO findUserByUsername(String username);
    Collection<String> getRolesByUsername(String username);
    void removeUserByUsername(String username);
    ProductToleranceResponseDTO userTolerateIngredients(String username, ProductToleranceInputDTO toleranceInputDTO);
}
