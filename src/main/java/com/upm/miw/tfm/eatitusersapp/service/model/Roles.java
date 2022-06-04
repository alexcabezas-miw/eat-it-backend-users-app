package com.upm.miw.tfm.eatitusersapp.service.model;

public enum Roles {
    ROLE_ADMIN,
    ROLE_DEFAULT_USER,
    ROLE_USER_CREATOR;

    public static boolean exist(String role) {
        try {
            Roles.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}