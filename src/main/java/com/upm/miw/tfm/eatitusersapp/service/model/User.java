package com.upm.miw.tfm.eatitusersapp.service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(User.USER_DOCUMENT_NAME)
public class User {
    public static final String USER_DOCUMENT_NAME = "users";

    @Id
    private String id;

    private String name;
    private String age;
    private String gender;
    private String nationality;
    private String username;
    private List<Roles> roles;
}
