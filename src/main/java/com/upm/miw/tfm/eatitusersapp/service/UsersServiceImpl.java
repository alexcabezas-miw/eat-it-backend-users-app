package com.upm.miw.tfm.eatitusersapp.service;

import com.upm.miw.tfm.eatitusersapp.exception.UserAlreadyExistValidationException;
import com.upm.miw.tfm.eatitusersapp.repository.UsersRepository;
import com.upm.miw.tfm.eatitusersapp.service.mapper.UsersMapper;
import com.upm.miw.tfm.eatitusersapp.service.model.User;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    public UsersServiceImpl(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    @Override
    public CreateUserDTO createUser(CreateUserDTO createUserDTO) {
        Optional<User> foundUserByUsername = this.usersRepository.findByUsername(createUserDTO.getUsername());
        if(foundUserByUsername.isPresent()) {
            throw new UserAlreadyExistValidationException(createUserDTO.getUsername());
        }

        User user = this.usersMapper.fromCreateUserDTO(createUserDTO);
        return this.usersMapper.toCreateUserDTO(this.usersRepository.save(user));
    }

    @Override
    public Collection<ListUserDTO> getAllUsers() {
        return this.usersRepository.findAll()
                .stream()
                .map(this.usersMapper::toLisUserDTO)
                .collect(Collectors.toList());
    }
}
