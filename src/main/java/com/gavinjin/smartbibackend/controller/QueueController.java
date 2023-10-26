package com.gavinjin.smartbibackend.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Test queue
 */
@RestController
@RequestMapping("/queue")
@Slf4j
@Profile({"dev", "local"})
public class QueueController {
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/add")
    public void add(String name) {
        CompletableFuture.runAsync(() -> {
            log.info("Task running: " + name + ", Executor: " + Thread.currentThread().getName());
            try {
                Thread.sleep(600000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, threadPoolExecutor);
    }

    @GetMapping("/get")
    public String get() {
        Map<String, Object> map = new HashMap<>();
        map.put("Queue size", threadPoolExecutor.getQueue().size());
        map.put("Total task count", threadPoolExecutor.getTaskCount());
        map.put("Completed task count", threadPoolExecutor.getCompletedTaskCount());
        map.put("Active thread count", threadPoolExecutor.getActiveCount());
        return JSONUtil.toJsonStr(map);
    }
}
