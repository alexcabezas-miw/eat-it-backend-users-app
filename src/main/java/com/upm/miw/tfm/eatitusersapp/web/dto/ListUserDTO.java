package com.upm.miw.tfm.eatitusersapp.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListUserDTO {
    private String id;

    private String username;

    private List<String> roles;
}
