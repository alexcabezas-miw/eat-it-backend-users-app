package com.upm.miw.tfm.eatitusersapp.repository;

import com.upm.miw.tfm.eatitusersapp.service.model.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {

}
