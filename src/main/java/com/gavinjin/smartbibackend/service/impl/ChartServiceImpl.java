package com.gavinjin.smartbibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gavinjin.smartbibackend.model.domain.Chart;
import com.gavinjin.smartbibackend.service.ChartService;
import com.gavinjin.smartbibackend.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author gavin
* @description 针对表【chart(Chart)】的数据库操作Service实现
* @createDate 2023-09-12 21:22:35
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {

}




