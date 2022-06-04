package com.upm.miw.tfm.eatitusersapp.config.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCredentialsDTO {
   private String username;
   private String password;
}
