package org.example.fitnessopenai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fitnessopenai.dtos.ChatCompletionRequest;
import org.example.fitnessopenai.dtos.ChatCompletionResponse;
import org.example.fitnessopenai.dtos.MyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Service
public class OpenAiService {

    public static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

    @Value("${openai.api-key}")
    private String API_KEY;


    public final static String URL = "https://api.openai.com/v1/chat/completions";
    public final static String MODEL = "gpt-4";
    public final static double TEMPERATURE = 0.8;
    public final static int MAX_TOKENS = 300;
    public final static double FREQUENCY_PENALTY = 0.0;
    public final static double PRESENCE_PENALTY = 0.0;
    public final static double TOP_P = 1.0;

    private WebClient client;

    public OpenAiService() {
        this.client = WebClient.create();
    }

    //Use this constructor for testing, to inject a mock client
    public OpenAiService(WebClient client) {
        this.client = client;
    }
// testing
    public MyResponse makeRequest(String userPrompt, String _systemMessage) {

        ChatCompletionRequest requestDto = new ChatCompletionRequest();
        requestDto.setModel(MODEL);
        requestDto.setTemperature(TEMPERATURE);
        requestDto.setMax_tokens(MAX_TOKENS);
        requestDto.setTop_p(TOP_P);
        requestDto.setFrequency_penalty(FREQUENCY_PENALTY);
        requestDto.setPresence_penalty(PRESENCE_PENALTY);
        requestDto.getMessages().add(new ChatCompletionRequest.Message("system", _systemMessage));
        requestDto.getMessages().add(new ChatCompletionRequest.Message("user", userPrompt));

    ObjectMapper mapper = new ObjectMapper();
    String json = "";
    String err =  null;
        try {
        json = mapper.writeValueAsString(requestDto);
        System.out.println(json);
            // Send the HTTP POST request using WebClient
            ChatCompletionResponse response = client.post()
                    .uri(new URI(URL)) // API endpoint URL for OpenAI.
                    .header("Authorization", "Bearer " + API_KEY) // Authorization header with API key.
                    .contentType(MediaType.APPLICATION_JSON) // Specify the content type as JSON.
                    .accept(MediaType.APPLICATION_JSON) // Expect JSON response from the API.
                    .body(BodyInserters.fromValue(json)) // Attach the JSON payload to the request body.
                    .retrieve() // Send the request and retrieve the response.
                    .bodyToMono(ChatCompletionResponse.class) // Convert the JSON response to ChatCompletionResponse object.
                    .block(); // Wait for the API call to complete and return the response.

        String responseMsg = response.getChoices().get(0).getMessage().getContent();
        int tokensUsed = response.getUsage().getTotal_tokens();
        System.out.print("Tokens used: " + tokensUsed);
        System.out.print(". Cost ($0.0015 / 1K tokens) : $" + String.format("%6f",(tokensUsed * 0.0015 / 1000)));
        System.out.println(". For 1$, this is the amount of similar requests you can make: " + Math.round(1/(tokensUsed * 0.0015 / 1000)));
        return new MyResponse(responseMsg);
    }
        catch (WebClientResponseException e){
        //This is how you can get the status code and message reported back by the remote API
        logger.error("Error response status code: " + e.getRawStatusCode());
        logger.error("Error response body: " + e.getResponseBodyAsString());
        logger.error("WebClientResponseException", e);
        err = "Internal Server Error, due to a failed request to external service. You could try again" +
                "( While you develop, make sure to consult the detailed error message on your backend)";
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
    }
        catch (Exception e) {
        logger.error("Exception", e);
        err = "Internal Server Error - You could try again" +
                "( While you develop, make sure to consult the detailed error message on your backend)";
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
    }
}


    public MyResponse getFitnessAdvice(String userInput) {
        String fitnessSystemMessage = "You are a virtual fitness coach that provides personalized fitness advice, training plans, and diet recommendations. " +
                "Understand the user's fitness goals, fitness level, and dietary preferences to offer supportive and practical guidance.";
        // Use the existing makeRequest method to send the quote request to OpenAI
        return makeRequest(userInput, fitnessSystemMessage);
    }




}

