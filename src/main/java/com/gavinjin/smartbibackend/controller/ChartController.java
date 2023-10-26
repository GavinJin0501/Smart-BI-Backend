package com.gavinjin.smartbibackend.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavinjin.smartbibackend.api.OpenAiApi;
import com.gavinjin.smartbibackend.api.YuCongmingApi;
import com.gavinjin.smartbibackend.manager.RedisLimiterManager;
import com.gavinjin.smartbibackend.model.domain.Chart;
import com.gavinjin.smartbibackend.model.domain.User;
import com.gavinjin.smartbibackend.model.dto.DeleteRequest;
import com.gavinjin.smartbibackend.model.dto.chart.*;
import com.gavinjin.smartbibackend.model.vo.BiResponse;
import com.gavinjin.smartbibackend.service.ChartService;
import com.gavinjin.smartbibackend.service.UserService;
import com.gavinjin.smartbibackend.util.ExcelUtils;
import com.gavinjin.smartbibackend.util.ResultUtils;
import com.gavinjin.smartbibackend.util.ThrowUtils;
import com.gavinjin.smartbibackend.util.common.BaseResponse;
import com.gavinjin.smartbibackend.util.common.ErrorCode;
import com.gavinjin.smartbibackend.util.exception.BusinessException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import static com.gavinjin.smartbibackend.util.constant.ChartConstant.*;

@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private OpenAiApi openAiApi;

    @Resource
    private YuCongmingApi yuCongmingApi;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    private final static Gson GSON = new Gson();

    /**
     * Create a chart
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * Delete a chart
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // Check if the chart exists
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // Only the owner or admin can delete the chart
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * Update the chart
     *
     * @param chartUpdateRequest
     * @return
     */
    @PostMapping("/update")
    // @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);

        long id = chartUpdateRequest.getId();
        // Check if the chart exists
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * Get chart by id
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Chart> getChartById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    /**
     * Get charts by pages
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                       HttpServletRequest request) {
        long current = chartQueryRequest.getCurrentPageNumber();
        long size = chartQueryRequest.getPageSize();
        // Restrict web scraping
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * Get charts created by current user
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                         HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrentPageNumber();
        long size = chartQueryRequest.getPageSize();
        // Restrict web scraping
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * Edit chart
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);

        User loginUser = userService.getLoginUser(request);
        long id = chartEditRequest.getId();
        // Check if the chart exists
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // Only the owner or admin can edit
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * Generate chart using AI
     *
     * @param multipartFile
     * @param genChartByAIRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAI(@RequestPart("file") MultipartFile multipartFile,
                                             GenChartByAIRequest genChartByAIRequest, HttpServletRequest request) {
        // Get chart info from user
        String name = genChartByAIRequest.getName();
        String goal = genChartByAIRequest.getGoal();
        String chartType = genChartByAIRequest.getChartType();

        // Validate user input
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "Goal is empty!");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "Name too long!");
        ThrowUtils.throwIf(StringUtils.isNotBlank(chartType) && chartType.length() > 100, ErrorCode.PARAMS_ERROR, "Chart type too long!");
        User loginUser = userService.getLoginUser(request);

        // Check Rate Limit: One RateLimiter for each user
        redisLimiterManager.doRateLimit("genChartByAI_ " + loginUser.getId());

        // Validate uploaded file name and size
        long size = multipartFile.getSize();
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "File size exceeds 1M");

        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffix = Arrays.asList("xlsx", "xls", "csv");
        ThrowUtils.throwIf(!validFileSuffix.contains(suffix), ErrorCode.PARAMS_ERROR, "File extension is invalid");

        // User Input
        // Combine goal, chartType with excelData
        StringBuilder userInput = new StringBuilder();
        userInput.append("Analysis goal:\n");
        if (StringUtils.isNotBlank(chartType)) {
            goal += ". Please use " + chartType;
        }
        userInput.append(goal).append("\n");
        String excelData = ExcelUtils.excelToString(multipartFile);
        userInput.append("Raw data:\n").append(excelData).append("\n");

        // Save the chart to db before using AI --> async
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(excelData);
        chart.setChartType(chartType);
        chart.setStatus(STATUS_WAIT);
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "Fail to save chart");

        // Submit an async task to the ThreadPoolExecutor
        try {
            CompletableFuture.runAsync(() -> {
                // Modify the task status to "running":
                // 1. Reduce the risk of repetitive execution
                // 2. Let the user know their task is being executed
                Chart updatedChart = chartService.getById(chart.getId());
                if (updatedChart == null) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR);
                } else if (!STATUS_WAIT.equals(updatedChart.getStatus())) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "Gen chart status is not waiting");
                }

                updatedChart.setStatus(STATUS_RUNNING);
                boolean b = chartService.updateById(updatedChart);
                if (!b) {
                    handleChartUpdateError(chart.getId(), "Fail to modify chart status to waiting");
                    return;
                }

                // 2 AI models to invoke
                String result = openAiApi.doChat(userInput.toString(), false);
                // String result = yuCongmingApi.doChat(YuCongmingApi.SMART_BI_ID, userInput.toString());

                String[] parts = result.split("【【【【【");
                if (parts.length < 3) {
                    handleChartUpdateError(chart.getId(), "AI generation error");
                    return;
                }
                String genChart = parts[1].trim();
                String genResult = parts[2].trim();

                Chart updateChartResult = new Chart();
                updateChartResult.setId(chart.getId());
                updateChartResult.setGenChart(genChart);
                updateChartResult.setGenResult(genResult);
                updateChartResult.setStatus(STATUS_SUCCEEDED);
                boolean updateResult = chartService.updateById(updateChartResult);
                if (!updateResult) {
                    handleChartUpdateError(chart.getId(), "Fail to modify chart status to succeeded");
                }
            }, threadPoolExecutor);
        } catch (RejectedExecutionException e) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Task queue error");
        }

        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        return ResultUtils.success(biResponse);
    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart chart = new Chart();
        chart.setId(chartId);
        chart.setStatus(STATUS_FAILED);
        chart.setExecMessage(execMessage);
        boolean result = chartService.updateById(chart);
        if (!result) {
            log.error("Fail to modify chart status to failed " + chartId + ": " + execMessage);
        }
    }
}
