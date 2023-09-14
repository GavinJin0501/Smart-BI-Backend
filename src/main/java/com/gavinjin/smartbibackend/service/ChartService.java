package com.gavinjin.smartbibackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gavinjin.smartbibackend.model.domain.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gavinjin.smartbibackend.model.dto.chart.ChartQueryRequest;

/**
* @author gavin
* @description 针对表【chart(Chart)】的数据库操作Service
* @createDate 2023-09-12 21:22:35
*/
public interface ChartService extends IService<Chart> {

    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);
}
