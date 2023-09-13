package com.gavinjin.smartbibackend.model.dto;

import java.io.Serializable;

/**
 * User update personal information request
 */
public class UserUpdateMyRequest implements Serializable {
    /**
     * Username
     */
    private String userName;

    /**
     * User avatar
     */
    private String userAvatar;

    private static final long serialVersionUID = 1L;
}
