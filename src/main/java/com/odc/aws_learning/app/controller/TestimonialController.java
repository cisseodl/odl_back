package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.TestimonialRequest;
import com.odc.aws_learning.app.dto.TestimonialResponse;
import com.odc.aws_learning.app.service.TestimonialService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/testimonials")
@RestController
@CrossOrigin(origins = {"https://smart-odc.com", "https://*.smart-odc.com", "https://api.smart-odc.com", "https://admin.smart-odc.com"}, maxAge = 3600)
public class TestimonialController {

    private final TestimonialService testimonialService;

    public TestimonialController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('APPRENANT', 'USER')") // Seuls les apprenants peuvent créer des témoignages
    public ResponseEntity<CResponse<TestimonialResponse>> addTestimonial(
            @Valid @RequestBody TestimonialRequest request,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return new ResponseEntity<>(CResponse.error("User not authenticated"), HttpStatus.UNAUTHORIZED);
        }
        CResponse<TestimonialResponse> response = testimonialService.addTestimonial(request, currentUser);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<CResponse<List<TestimonialResponse>>> getAllTestimonials() {
        System.out.println("=== TestimonialController.getAllTestimonials() appelé ===");
        CResponse<List<TestimonialResponse>> response = testimonialService.getAllTestimonials();
        System.out.println("=== Nombre de témoignages: " + (response.getData() != null ? response.getData().size() : 0) + " ===");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CResponse<List<TestimonialResponse>>> getTestimonialsByUser(@PathVariable Long userId) {
        CResponse<List<TestimonialResponse>> response = testimonialService.getTestimonialsByUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
