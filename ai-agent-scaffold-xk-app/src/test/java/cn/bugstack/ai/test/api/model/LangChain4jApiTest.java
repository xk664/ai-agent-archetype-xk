package cn.bugstack.ai.test.api.model;

import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;

/**
 * LangChain4j
 * <p>
 * 文档：<a href="https://docs.langchain4j.info/">langchain4j</a>
 * 案例：<a href="https://github.com/langchain4j/langchain4j-examples">langchain4j-examples</a>
 *
 * @author xiaofuge bugstack.cn @小傅哥
 * 2025/12/14 09:20
 */
@Slf4j
public class LangChain4jApiTest {

    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey("sk-56eef28e431a4cdebe93756cd120b899")
                .modelName("deepseek-chat")
                .build();

        String chat = model.chat("你是谁");
        log.info("测试结果:{}", chat);
    }

}
