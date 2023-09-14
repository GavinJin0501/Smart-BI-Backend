package com.gavinjin.smartbibackend.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * Chart update request
 *
 */
@Data
public class ChartUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * Analysis goal
     */
    private String goal;

    /**
     * Data of chart
     */
    private String chartData;

    /**
     * Chart type
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}