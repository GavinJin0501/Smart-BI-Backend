package com.gavinjin.smartbibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gavinjin.smartbibackend.mapper.ChartMapper;
import com.gavinjin.smartbibackend.model.domain.Chart;
import com.gavinjin.smartbibackend.model.dto.chart.ChartQueryRequest;
import com.gavinjin.smartbibackend.service.ChartService;
import com.gavinjin.smartbibackend.util.SqlUtils;
import com.gavinjin.smartbibackend.util.constant.CommonConstant;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.gavinjin.smartbibackend.util.constant.ChartConstant.*;

/**
* @author gavin
* @description 针对表【chart(Chart)】的数据库操作Service实现
* @createDate 2023-09-12 21:22:35
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {
    /**
     * Get chart query wrapper
     *
     * @param chartQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }

        Long id = chartQueryRequest.getId();
        String name = chartQueryRequest.getName();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        Long userId = chartQueryRequest.getUserId();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();

        queryWrapper.eq(id != null && id > 0, DB_COL_CHART_ID, id);
        queryWrapper.like(ObjectUtils.isNotEmpty(name), DB_COL_NAME, name);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), DB_COL_USER_ID, userId);
        queryWrapper.eq(StringUtils.isNotBlank(goal), DB_COL_CHART_GOAL, goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), DB_COL_CHART_TYPE, chartType);
        queryWrapper.eq(DB_COL_IS_DELETED, false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




