package com.upm.miw.tfm.eatitusersapp.service.client.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restriction {
    private Collection<String> ingredients;
}
