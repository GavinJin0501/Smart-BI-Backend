package com.gavinjin.smartbibackend.model.dto.user;

import lombok.Getter;

import java.io.Serializable;

/**
 * User register request
 */
@Getter
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
