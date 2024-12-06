package org.example.fitnessopenai;


import org.example.fitnessopenai.api.FitnessController;
import org.example.fitnessopenai.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {FitnessController.class, OpenAiService.class})
class FitnessOpenAiApplicationTests {

    @Test
    void contextLoads() {
    }

}
