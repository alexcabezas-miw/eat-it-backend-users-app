package com.upm.miw.tfm.eatitusersapp.service.users;

import com.upm.miw.tfm.eatitusersapp.exception.RoleDoesNotExistValidationException;
import com.upm.miw.tfm.eatitusersapp.exception.UserAlreadyExistValidationException;
import com.upm.miw.tfm.eatitusersapp.exception.UserDoesNotExistValidationException;
import com.upm.miw.tfm.eatitusersapp.repository.UsersRepository;
import com.upm.miw.tfm.eatitusersapp.service.client.product.ProductClientFacade;
import com.upm.miw.tfm.eatitusersapp.service.mapper.UsersMapper;
import com.upm.miw.tfm.eatitusersapp.service.model.Roles;
import com.upm.miw.tfm.eatitusersapp.service.model.User;
import com.upm.miw.tfm.eatitusersapp.web.dto.*;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final ProductClientFacade productClientFacade;

    private final static StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();

    public UsersServiceImpl(UsersRepository usersRepository,
                            UsersMapper usersMapper,
                            ProductClientFacade productClientFacade) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.productClientFacade = productClientFacade;
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
    @Cacheable("usersByUsername")
    public ListUserDTO findUserByUsername(String username) {
        Optional<User> user = this.usersRepository.findByUsername(username);
        return user.map(this.usersMapper::toLisUserDTO)
                .orElseThrow(() -> new UserDoesNotExistValidationException(username));
    }

    @Override
    @Cacheable("roles")
    public Collection<String> getRolesByUsername(String username) {
        Optional<User> user = this.usersRepository.findByUsername(username);
        if(user.isEmpty()) {
            return Collections.emptyList();
        }
        return user.get().getRoles()
                .stream()
                .map(Roles::name).collect(Collectors.toList());
    }

    @Override
    public void removeUserByUsername(String username) {
        Optional<User> user = this.usersRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UserDoesNotExistValidationException(username);
        }
        this.usersRepository.delete(user.get());
    }

    @Override
    public ProductToleranceResponseDTO userTolerateIngredients(String username, ProductToleranceInputDTO toleranceInputDTO) {
        User user = this.usersRepository.findByUsername(username)
                .orElseThrow(() -> new UserDoesNotExistValidationException(username));

        Collection<String> combinedRestrictedIngredients = getCombinedRestrictedIngredients(user);
        List<String> blockingIngredients = toleranceInputDTO.getIngredients().stream()
                .filter(combinedRestrictedIngredients::contains).collect(Collectors.toList());

        if(blockingIngredients.isEmpty()) {
            return ProductToleranceResponseDTO.valid();
        }
        return ProductToleranceResponseDTO.invalid(blockingIngredients);
    }

    Collection<String> getCombinedRestrictedIngredients(User user) {
        List<String> restrictions = user.getRestrictions() == null ? Collections.emptyList() : user.getRestrictions();
        List<String> userIngredients = user.getRestrictedIngredients() == null ? Collections.emptyList() : user.getRestrictedIngredients();
        Stream<String> ingredientsFromRestrictionsStream = restrictions.stream()
                .flatMap(restriction -> this.productClientFacade.getRestrictionIngredients(restriction).stream())
                .distinct();

        return Stream.concat(ingredientsFromRestrictionsStream, userIngredients.stream()).distinct()
                .collect(Collectors.toList());
    }
}
