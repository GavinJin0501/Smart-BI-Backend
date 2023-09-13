package com.gavinjin.smartbibackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.gavinjin.smartbibackend.mapper")
@SpringBootApplication
public class SmartBiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartBiBackendApplication.class, args);
    }

}
