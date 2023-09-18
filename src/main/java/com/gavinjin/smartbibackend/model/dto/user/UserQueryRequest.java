package com.gavinjin.smartbibackend.model.dto.user;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * User query request
 */
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * Username
     */
    private String userName;

    /**
     * User role：user/admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
