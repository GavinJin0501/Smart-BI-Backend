package com.gavinjin.smartbibackend.model.dto.chart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Char edit request
 */
@Data
public class ChartEditRequest implements Serializable {
    /**
     * Chart name
     */
    private String name;


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