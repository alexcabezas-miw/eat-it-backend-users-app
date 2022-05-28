package com.upm.miw.tfm.eatitusersapp.service;

import com.upm.miw.tfm.eatitusersapp.repository.UsersRepository;
import com.upm.miw.tfm.eatitusersapp.service.mapper.UsersMapper;
import com.upm.miw.tfm.eatitusersapp.service.model.User;
import com.upm.miw.tfm.eatitusersapp.web.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    public UsersServiceImpl(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = this.usersMapper.toEntity(userDTO);
        return this.usersMapper.toDTO(this.usersRepository.save(user));
    }
}
