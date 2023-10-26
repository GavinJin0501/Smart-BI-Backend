package com.gavinjin.smartbibackend.mapper;

import com.gavinjin.smartbibackend.model.domain.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author gavin
* @description 针对表【chart(Chart)】的数据库操作Mapper
* @createDate 2023-09-12 21:22:35
* @Entity generator.domain.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {
    List<Map<String, Object>> queryChartData(String querySql);
}




