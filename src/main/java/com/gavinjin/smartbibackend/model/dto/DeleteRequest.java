package com.gavinjin.smartbibackend.model.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * User delete request
 */
@Getter
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
