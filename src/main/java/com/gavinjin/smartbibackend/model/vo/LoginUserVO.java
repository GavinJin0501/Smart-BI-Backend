package com.gavinjin.smartbibackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Logged in user vo (sensitive data removed)
 */
@Data
public class LoginUserVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * Account
     */
    private String userAccount;

    /**
     * Nickname
     */
    private String userName;

    /**
     * Avatar
     */
    private String userAvatar;

    /**
     * Role：user/admin
     */
    private String userRole;

    /**
     * Created Time
     */
    private Date createTime;

    /**
     * Updated time
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
