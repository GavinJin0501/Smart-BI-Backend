package com.gavinjin.smartbibackend.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenChartByAIRequest implements Serializable {
    /**
     * Chart name
     */
    private String name;

    /**
     * Analysis goal
     */
    private String goal;

    /**
     * Chart type
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}
