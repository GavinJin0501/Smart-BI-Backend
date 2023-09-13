package com.gavinjin.smartbibackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User vo (sensitive data removed)
 */
@Data
public class UserVO implements Serializable {
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

    /**
     * Create time
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
