package com.upm.miw.tfm.eatitusersapp.web.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserInputDTO {

    @NotBlank(message = "Username can not be null or empty!")
    private String username;

    @NotBlank(message = "name can not be null or empty!")
    private String name;

    @NotBlank(message = "age can not be null or empty!")
    private String age;

    @NotBlank(message = "gender can not be null or empty!")
    private String gender;

    @NotBlank(message = "nationality can not be null or empty!")
    private String nationality;

    @NotBlank(message = "password can not be null or empty!")
    private String password;

    @Builder.Default
    private List<String> roles = new ArrayList<>();
}
