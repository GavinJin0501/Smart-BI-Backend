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
 * Chart add request
 */
@Data
public class ChartAddRequest implements Serializable {

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