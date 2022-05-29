package com.upm.miw.tfm.eatitusersapp.service.mapper;

import com.upm.miw.tfm.eatitusersapp.service.model.User;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    CreateUserDTO toCreateUserDTO(User user);

    User fromCreateUserDTO(CreateUserDTO createUserDTO);

    ListUserDTO toLisUserDTO(User user);
}
