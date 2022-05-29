package com.upm.miw.tfm.eatitusersapp.service;

import com.upm.miw.tfm.eatitusersapp.exception.UserAlreadyExistValidationException;
import com.upm.miw.tfm.eatitusersapp.exception.ValidationException;
import com.upm.miw.tfm.eatitusersapp.repository.UsersRepository;
import com.upm.miw.tfm.eatitusersapp.service.mapper.UsersMapper;
import com.upm.miw.tfm.eatitusersapp.service.model.User;
import com.upm.miw.tfm.eatitusersapp.web.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<User> foundUserByUsername = this.usersRepository.findByUsername(userDTO.getUsername());
        if(foundUserByUsername.isPresent()) {
            throw new UserAlreadyExistValidationException(userDTO.getUsername());
        }

        User user = this.usersMapper.toEntity(userDTO);
        return this.usersMapper.toDTO(this.usersRepository.save(user));
    }
}
