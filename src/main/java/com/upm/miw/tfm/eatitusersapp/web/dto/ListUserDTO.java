package com.upm.miw.tfm.eatitusersapp.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListUserDTO {

    private String username;

    private String name;

    private String age;

    private String gender;

    private String nationality;

    @Builder.Default
    private Collection<String> restrictedIngredients = new ArrayList<>();

    @Builder.Default
    private Collection<String> restrictions = new ArrayList<>();
}
