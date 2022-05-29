package com.upm.miw.tfm.eatitusersapp.service.model;

public enum Roles {
    ADMIN,
    DEFAULT_USER;

    public static boolean exist(String role) {
        try {
            Roles.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}