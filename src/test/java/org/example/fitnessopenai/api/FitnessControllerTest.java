package org.example.fitnessopenai.api;

import org.example.fitnessopenai.service.OpenAiService;
import org.example.fitnessopenai.dtos.MyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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

        mockMvc.perform(post("/fitness/advice")
                        .contentType(APPLICATION_JSON)
                        .content("{\"userInput\": \"I want to get stronger\"}")) // JSON body
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.answer").value("Focus on compound exercises and a balanced diet.")); // Check response
    }
}
