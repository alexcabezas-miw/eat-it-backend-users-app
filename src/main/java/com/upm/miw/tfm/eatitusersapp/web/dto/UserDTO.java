package com.upm.miw.tfm.eatitusersapp.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    private String id;

    @NotBlank(message = "Username can not be null or empty!")
    private String username;
}
