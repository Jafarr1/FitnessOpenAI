package org.example.fitnessopenai.api;

import org.example.fitnessopenai.service.OpenAiService;
import org.example.fitnessopenai.dtos.MyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FitnessController.class)
class FitnessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenAiService openAiService;


    @Test
    void getFitnessAdvice_ReturnsAdvice_WhenGivenValidInput() throws Exception {
        String userInput = "I want to get stronger";
        MyResponse mockResponse = new MyResponse("Focus on compound exercises and a balanced diet.");

        when(openAiService.getFitnessAdvice(userInput)).thenReturn(mockResponse);

        mockMvc.perform(get("/fitness/advice").param("userInput", userInput))
                .andExpect(status().isOk());
               // .andExpect(content().string(containsString("Focus on compound exercises and a balanced diet.")));
    }

}
