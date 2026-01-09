package com.odc.aws_learning.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.odc.aws_learning.app.entity.Apprenant; // Ajouté

import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> test(@RequestBody Map<String, Object> body) {
        return body;
    }

    @PostMapping(value = "/apprenant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Apprenant testApprenant(@RequestBody Apprenant apprenantDetails) {
        return apprenantDetails;
    }
}
