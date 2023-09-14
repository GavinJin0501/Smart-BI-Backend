package com.gavinjin.smartbibackend.model.dto.chart;

import com.gavinjin.smartbibackend.model.dto.user.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Chart query request
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * Analysis goal
     */
    private String goal;

    /**
     * Chart type
     */
    private String chartType;

    /**
     * User id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}