package com.upm.miw.tfm.eatitusersapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductToleranceResponseDTO {

    private boolean canEatIt;

    private Collection<String> blockingIngredients = new ArrayList<>();

    public static ProductToleranceResponseDTO invalid(Collection<String> blockingIngredients) {
        return new ProductToleranceResponseDTO(false, blockingIngredients);
    }

    public static ProductToleranceResponseDTO valid() {
        return new ProductToleranceResponseDTO(true, Collections.emptyList());
    }
}
