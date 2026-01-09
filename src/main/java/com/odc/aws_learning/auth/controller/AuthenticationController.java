package com.odc.aws_learning.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.odc.aws_learning.auth.dao.request.SignUpRequest;
import com.odc.aws_learning.auth.dao.request.SigninRequest;
import com.odc.aws_learning.auth.dao.request.UpdatePass;
import com.odc.aws_learning.auth.dao.response.JwtAuthenticationResponse;
import com.odc.aws_learning.auth.service.AuthenticationService;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.http.MediaType;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CResponse<JwtAuthenticationResponse> signup(@RequestParam("user") String userString,
                                                       @RequestParam(value = "avatar", required = false)MultipartFile avatar) throws JsonProcessingException {
        SignUpRequest request = new ObjectMapper().readValue(userString, SignUpRequest.class);
        return authenticationService.signup(request, avatar);
    }

    @PostMapping("/signin")
    public CResponse<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return authenticationService.signin(request);
    }

    @GetMapping("/forget-pass/{username}")
    public CResponse<?> forgotPassword(@PathVariable String username) {
        return authenticationService.forgetPass(username);
    }

    @PostMapping("/change-pass")
    CResponse<?> changePass(@RequestBody UpdatePass updatePass) {
        return authenticationService.updatePassword(updatePass);
    }



    @GetMapping("/check-availability")
    public String availability() {
        return "ATK Rest Api works fine";
    }
}
