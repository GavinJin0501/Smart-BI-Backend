package com.gavinjin.smartbibackend.model.dto.user;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * User add request
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * Username
     */
    private String userName;

    /**
     * User account
     */
    private String userAccount;

    /**
     * User avatar
     */
    private String userAvatar;

    /**
     * User role: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}