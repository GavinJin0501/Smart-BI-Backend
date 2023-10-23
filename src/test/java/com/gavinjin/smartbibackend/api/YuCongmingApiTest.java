package com.gavinjin.smartbibackend.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class YuCongmingApiTest {
    @Resource
    private YuCongmingApi yuCongmingApi;

    @Test
    void testDoChat() {
        String answer = yuCongmingApi.doChat(YuCongmingApi.SMART_BI_ID, "分析网站用户增长趋势\n" +
                "日期,人数\n" +
                "1,10\n" +
                "2,20\n" +
                "3,30");
        System.out.println(answer);
    }
}
