package com.gavinjin.smartbibackend.model.vo;

import lombok.Data;

/**
 * BI response
 */
@Data
public class BiResponse {
    private String genChart;
    private String genResult;
    private Long chartId;
}
