package com.gavinjin.smartbibackend.model.dto;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * User delete request
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
