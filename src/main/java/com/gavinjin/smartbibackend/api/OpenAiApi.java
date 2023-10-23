package com.gavinjin.smartbibackend.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiApi {
    // Use application.yml to decouple configuration
    private final static String URL = "https://api.openai.com/v1/chat/completions";
    private final static String SECRET_KEY = System.getenv("OPENAI_API_KEY") == null ? "" : System.getenv("OPENAI_API_KEY");
    private final static String SETTING = "You are a data analyst and frontend development expert. I will give you data in the following format:\n" +
            "Analysis goal:\n" +
            "{The requirements or goal of data analysis}\n" +
            "Raw data:\n" +
            "{Data in csv format separated by comma}\n" +
            "Based on the above two parts, please generate a response in the following format (don't generate any additional opening, ending. and comments).\n" +
            "【【【【【\n" +
            "{The option configuration JSON code of Frontend EChart V5, which reasonably visualize the data. Don't generate unnecessary content like comments}\n" +
            "【【【【【\n" +
            "{Detailed data analysis report. Don't generate unnecessary comments}\n";
    private final static String DEFAULT_RESULT = "{\n" +
            "  \"id\": \"chatcmpl-80LvEIr1TTWs2U6iijo5ce0Y54KUR\",\n" +
            "  \"object\": \"chat.completion\",\n" +
            "  \"created\": 1695094960,\n" +
            "  \"model\": \"gpt-3.5-turbo-0613\",\n" +
            "  \"choices\": [\n" +
            "    {\n" +
            "      \"index\": 0,\n" +
            "      \"message\": {\n" +
            "        \"role\": \"assistant\",\n" +
            "        \"content\": \"【【【【【\\n{\\n  \\\"xAxis\\\": {\\n    \\\"type\\\": \\\"category\\\",\\n    \\\"data\\\": [\\\"1\\\", \\\"2\\\", \\\"3\\\", \\\"4\\\", \\\"5\\\"],\\n    \\\"name\\\": \\\"Date\\\"\\n  },\\n  \\\"yAxis\\\": {\\n    \\\"type\\\": \\\"value\\\",\\n    \\\"name\\\": \\\"User Number\\\"\\n  },\\n  \\\"series\\\": [{\\n    \\\"type\\\": \\\"line\\\",\\n    \\\"data\\\": [10, 20, 30, 40, 50],\\n    \\\"name\\\": \\\"User Number\\\"\\n  }],\\n  \\\"title\\\": {\\n    \\\"text\\\": \\\"Website User Growth Trend\\\"\\n  }\\n}\\n【【【【【\\nThe website user growth trend can be visualized using a line chart. The x-axis represents the dates, and the y-axis represents the number of users. The line chart shows the increasing trend of the user numbers over time.\\n\\nAccording to the provided data, the user numbers on the website have been growing steadily. As seen from the line chart, the number of users increased from 10 on day 1 to 20 on day 2, 30 on day 3, 40 on day 4, and 50 on day 5.\\n\\nThis indicates a linear growth trend, where the number of users is increasing by 10 every day. The website seems to be attracting more users and experiencing positive growth.\\n\\nTo further analyze and understand the growth trend, it would be helpful to track additional metrics such as user acquisition channels, user demographics, and conversion rates. This information can provide insights into the factors driving the growth and enable optimization strategies to be implemented accordingly.\\n\\nOverall, based on the current data, the website is showing a positive growth trend in terms of user numbers, which is a positive indicator for the business.\"\n" +
            "      },\n" +
            "      \"finish_reason\": \"stop\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"usage\": {\n" +
            "    \"prompt_tokens\": 174,\n" +
            "    \"completion_tokens\": 361,\n" +
            "    \"total_tokens\": 535\n" +
            "  }\n" +
            "}";

    /**
     * Wrap the request to a json string
     *
     * @param userMessage
     * @return
     */
    private String createRequestBodyJson(String userMessage) {
        List<Map<String, String>> list =new ArrayList<>();
        Map<String, String> sys = new HashMap<>();
        sys.put("role", "system");
        sys.put("content", SETTING);
        Map<String, String> user = new HashMap<>();
        user.put("role", "user");
        user.put("content", userMessage);
        list.add(sys);
        list.add(user);

        Map<String, Object> map = new HashMap<>();
        map.put("model", "gpt-3.5-turbo");
        map.put("messages", list);

        return JSONUtil.toJsonStr(map);
    }

    /**
     * Parse the response from openai
     *
     * @param response
     * @return
     */
    private String parseResultFromOpenApi(String response) {
        JSONObject jsonObj = JSONUtil.parseObj(response);
        JSONArray choices = JSONUtil.parseArray(jsonObj.get("choices").toString());
        JSONObject message = JSONUtil.parseObj(JSONUtil.parseObj(choices.get(0)).get("message"));
        return message.get("content").toString();
    }

    /**
     * Request the openai for response
     *
     * @param userMessage
     * @return
     */
    public String doChat(String userMessage, boolean isTest) {
        // 1. Create request message
        String json = new OpenAiApi().createRequestBodyJson(userMessage);

        // 2. Invoke openai api
        String response = (isTest) ? DEFAULT_RESULT : HttpRequest.post(URL)
                .header("Authorization", "Bearer " + SECRET_KEY)
                .body(json)
                .execute()
                .body();

        // 3. Parse the response and return the data string
        return parseResultFromOpenApi(response);
    }
}

