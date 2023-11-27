package com.gavinjin.smartbibackend.business_mq;

import com.gavinjin.smartbibackend.api.OpenAiApi;
import com.gavinjin.smartbibackend.model.domain.Chart;
import com.gavinjin.smartbibackend.service.ChartService;
import com.gavinjin.smartbibackend.util.ExcelUtils;
import com.gavinjin.smartbibackend.util.common.ErrorCode;
import com.gavinjin.smartbibackend.util.exception.BusinessException;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.gavinjin.smartbibackend.business_mq.BiMqConstant.BI_QUEUE_NAME;
import static com.gavinjin.smartbibackend.util.constant.ChartConstant.*;

@Slf4j
@Component
public class BiMessageConsumer {
    @Resource
    private ChartService chartService;

    @Resource
    private OpenAiApi openAiApi;

    @SneakyThrows
    @RabbitListener(queues = {BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "message is empty");
        }

        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "chart is empty");
        }

        // Modify the task status to "running":
        // 1. Reduce the risk of repetitive execution
        // 2. Let the user know their task is being executed
        Chart updatedChart = new Chart();
        updatedChart.setId(chart.getId());
        updatedChart.setStatus(STATUS_RUNNING);
        boolean b = chartService.updateById(updatedChart);
        if (!b) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "Fail to modify chart status to waiting");
            return;
        }

        // 2 AI models to invoke
        String result = openAiApi.doChat(buildUserInput(chart), false);
        String[] parts = result.split("【【【【【");
        if (parts.length < 3) {
            channel.basicNack(deliveryTag, false, false);
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
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "Fail to modify chart status to succeeded");
        }

        // Ack the message if all succeed
        channel.basicAck(deliveryTag, false);
    }

    /**
     * Build user inpout
     * @param chart
     * @return
     */
    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // User Input
        // Combine goal, chartType with excelData
        StringBuilder userInput = new StringBuilder();
        userInput.append("Analysis goal:\n");
        if (StringUtils.isNotBlank(chartType)) {
            goal += ". Please use " + chartType;
        }
        userInput.append(goal).append("\n");
        userInput.append("Raw data:\n").append(csvData).append("\n");

        return userInput.toString();
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
