package com.upm.miw.tfm.eatitusersapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductToleranceInputDTO {

    @Builder.Default
    private Collection<String> ingredients = new ArrayList<>();
}
