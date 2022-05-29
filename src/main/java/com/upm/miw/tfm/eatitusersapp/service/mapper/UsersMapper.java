package com.upm.miw.tfm.eatitusersapp.service.mapper;

import com.upm.miw.tfm.eatitusersapp.service.model.User;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserOutputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    User fromCreateUserInputDTO(CreateUserInputDTO createUserInputDTO);

    ListUserDTO toLisUserDTO(User user);

    CreateUserOutputDTO toCreateUserOutputDTO(User user);
}
