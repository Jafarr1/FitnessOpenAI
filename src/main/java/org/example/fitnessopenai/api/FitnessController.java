package org.example.fitnessopenai.api;

import org.example.fitnessopenai.service.OpenAiService;
import org.example.fitnessopenai.dtos.MyResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class FitnessController {

    private OpenAiService service;

    public FitnessController(OpenAiService service) {
        this.service = service;
    }

    @PostMapping("/fitness/advice")
    public MyResponse getFitnessAdvice(@RequestBody Map<String, String> request) {
        String userInput = request.get("userInput");
        return service.getFitnessAdvice(userInput);
    }

}

