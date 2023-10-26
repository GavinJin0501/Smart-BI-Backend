package com.gavinjin.smartbibackend.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {
    @Resource
    private ChartMapper chartMapper;

    @Test
    void queryChartData() {
        String chartId = "1716625017015414786";
        String querySql = String.format("select * from chart_%s", chartId);
        List<Map<String, Object>> result = chartMapper.queryChartData(querySql);
        System.out.println(result);
    }
}