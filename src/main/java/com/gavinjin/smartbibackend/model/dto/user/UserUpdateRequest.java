package com.gavinjin.smartbibackend.model.dto.user;

import lombok.Getter;

/**
 * User update request
 */
@Getter
public class UserUpdateRequest {
    /**
     * id
     */
    private Long id;

    /**
     * Username
     */
    private String userName;

    /**
     * User avatar
     */
    private String userAvatar;


    /**
     * User role：user/admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
