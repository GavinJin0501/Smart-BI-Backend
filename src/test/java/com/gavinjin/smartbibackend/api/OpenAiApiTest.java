package com.gavinjin.smartbibackend.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class OpenAiApiTest {
    private final static String USER_INPUT = "Analysis goal:\n" +
            "Analyze the growing trend of the website\n" +
            "Raw data:\n" +
            "Date,User Number\n" +
            "1,10\n" +
            "2,20\n" +
            "3,30\n" +
            "4,40\n" +
            "5,50";
    private final static String DEFAULT_CONTENT = "【【【【【\n" +
            "{\n" +
            "  \"xAxis\": {\n" +
            "    \"type\": \"category\",\n" +
            "    \"data\": [\"1\", \"2\", \"3\", \"4\", \"5\"],\n" +
            "    \"name\": \"Date\"\n" +
            "  },\n" +
            "  \"yAxis\": {\n" +
            "    \"type\": \"value\",\n" +
            "    \"name\": \"User Number\"\n" +
            "  },\n" +
            "  \"series\": [{\n" +
            "    \"type\": \"line\",\n" +
            "    \"data\": [10, 20, 30, 40, 50],\n" +
            "    \"name\": \"User Number\"\n" +
            "  }],\n" +
            "  \"title\": {\n" +
            "    \"text\": \"Website User Growth Trend\"\n" +
            "  }\n" +
            "}\n" +
            "【【【【【\n" +
            "The website user growth trend can be visualized using a line chart. The x-axis represents the dates, and the y-axis represents the number of users. The line chart shows the increasing trend of the user numbers over time.\n" +
            "\n" +
            "According to the provided data, the user numbers on the website have been growing steadily. As seen from the line chart, the number of users increased from 10 on day 1 to 20 on day 2, 30 on day 3, 40 on day 4, and 50 on day 5.\n" +
            "\n" +
            "This indicates a linear growth trend, where the number of users is increasing by 10 every day. The website seems to be attracting more users and experiencing positive growth.\n" +
            "\n" +
            "To further analyze and understand the growth trend, it would be helpful to track additional metrics such as user acquisition channels, user demographics, and conversion rates. This information can provide insights into the factors driving the growth and enable optimization strategies to be implemented accordingly.\n" +
            "\n" +
            "Overall, based on the current data, the website is showing a positive growth trend in terms of user numbers, which is a positive indicator for the business.\n";

    @Resource
    private OpenAiApi openAiApi;

    @BeforeEach
    void createApiInstance() {
        openAiApi = new OpenAiApi();
    }

    @Test
    void testDoChat() {
        System.out.println(openAiApi.doChat(USER_INPUT, true));
    }

    @Test
    void testParse() {
        String[] split = DEFAULT_CONTENT.split("【【【【【");
        assert split.length >= 2;

        for (int i = 0; i < split.length; i++) {
            System.out.println(i + ":" + split[i]);
        }
    }
}
