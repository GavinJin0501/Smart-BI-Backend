package com.gavinjin.smartbibackend.util.common;

import lombok.Getter;

/**
 * Enum of user roles: user/admin
 */
@Getter
public enum UserRole {
    USER("用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;

    UserRole(String text, String value) {
        this.text = text;
        this.value = value;
    }
}
