package com.upm.miw.tfm.eatitusersapp.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CreateUserInputDTO {

    @NotBlank(message = "Username can not be null or empty!")
    private String username;

    @Builder.Default
    private List<String> roles = new ArrayList<>();
}
