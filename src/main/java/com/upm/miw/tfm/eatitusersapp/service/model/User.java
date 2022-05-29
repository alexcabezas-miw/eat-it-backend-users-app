package com.upm.miw.tfm.eatitusersapp.service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(User.USER_DOCUMENT_NAME)
public class User {
    public static final String USER_DOCUMENT_NAME = "users";

    @Id
    private String id;

    private String username;
}
