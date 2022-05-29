package com.upm.miw.tfm.eatitusersapp.service.mapper;

import com.upm.miw.tfm.eatitusersapp.service.model.User;
import com.upm.miw.tfm.eatitusersapp.web.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

}
