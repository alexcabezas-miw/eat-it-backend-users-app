package com.upm.miw.tfm.eatitusersapp.repository;

import com.upm.miw.tfm.eatitusersapp.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<User, Integer> {
}
