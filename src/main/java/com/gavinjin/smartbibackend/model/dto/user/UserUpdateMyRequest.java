package com.gavinjin.smartbibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * User update personal information request
 */
@Data
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
