package com.upm.miw.tfm.eatitusersapp.repository;

import com.upm.miw.tfm.eatitusersapp.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsersRepository extends MongoRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
