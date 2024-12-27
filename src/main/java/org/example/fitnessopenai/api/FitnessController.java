package org.example.fitnessopenai.api;

import org.example.fitnessopenai.service.OpenAiService;
import org.example.fitnessopenai.dtos.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "https://ashy-moss-01592bf10.5.azurestaticapps.net")
@RestController
public class FitnessController {

    private OpenAiService service;

    @Autowired
    public FitnessController(OpenAiService service) {
        this.service = service;
    }

    @PostMapping("/fitness/advice")
    public MyResponse getFitnessAdvice(@RequestBody Map<String, String> request) {
        String userInput = request.get("userInput");
        return service.getFitnessAdvice(userInput);
    }

}

