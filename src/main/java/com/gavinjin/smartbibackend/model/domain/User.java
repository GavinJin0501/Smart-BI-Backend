package com.gavinjin.smartbibackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * Account
     */
    private String userAccount;

    /**
     * Password
     */
    private String userPassword;

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

    /**
     * Is deleted?
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}