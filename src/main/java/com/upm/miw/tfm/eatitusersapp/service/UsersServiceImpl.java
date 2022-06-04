package com.upm.miw.tfm.eatitusersapp.service;

import com.upm.miw.tfm.eatitusersapp.exception.RoleDoesNotExistValidationException;
import com.upm.miw.tfm.eatitusersapp.exception.UserAlreadyExistValidationException;
import com.upm.miw.tfm.eatitusersapp.exception.UserDoesNotExistValidationException;
import com.upm.miw.tfm.eatitusersapp.repository.UsersRepository;
import com.upm.miw.tfm.eatitusersapp.service.mapper.UsersMapper;
import com.upm.miw.tfm.eatitusersapp.service.model.Roles;
import com.upm.miw.tfm.eatitusersapp.service.model.User;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserInputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.CreateUserOutputDTO;
import com.upm.miw.tfm.eatitusersapp.web.dto.ListUserDTO;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    private final static StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();

    public UsersServiceImpl(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    @Override
    public CreateUserOutputDTO createUser(CreateUserInputDTO createUserInputDTO) {
        Optional<User> foundUserByUsername = this.usersRepository.findByUsername(createUserInputDTO.getUsername());
        if(foundUserByUsername.isPresent()) {
            throw new UserAlreadyExistValidationException(createUserInputDTO.getUsername());
        }

        User user = this.usersMapper.fromCreateUserInputDTO(createUserInputDTO);
        user.setPassword(encryptor.encryptPassword(user.getPassword()));
        return this.usersMapper.toCreateUserOutputDTO(this.usersRepository.save(user));
    }

    @Override
    public Collection<ListUserDTO> getAllUsers() {
        return this.usersRepository.findAll()
                .stream()
                .map(this.usersMapper::toLisUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void editRolesByUsername(String username, Collection<String> roles) {
        Optional<User> user = this.usersRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UserDoesNotExistValidationException(username);
        }

        roles.forEach(role -> {
            if(!Roles.exist(role)) {
                throw new RoleDoesNotExistValidationException(role);
            }
        });

        List<Roles> mappedRoles = roles.stream()
                .map(Roles::valueOf)
                .collect(Collectors.toList());
        user.get().setRoles(mappedRoles);

        this.usersRepository.save(user.get());
    }

    @Override
    public ListUserDTO findUserByUsername(String username) {
        Optional<User> user = this.usersRepository.findByUsername(username);
        return user.map(this.usersMapper::toLisUserDTO)
                .orElseThrow(() -> new UserDoesNotExistValidationException(username));
    }
}
