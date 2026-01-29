package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.dto.TestimonialRequest;
import com.odc.aws_learning.app.dto.TestimonialResponse;
import com.odc.aws_learning.app.entity.Testimonial;
import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.repository.TestimonialRepository;
import com.odc.aws_learning.app.repository.ApprenantRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Correct import for Collectors.toList()

@Service
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final UserRepository userRepository;
    private final ApprenantRepository apprenantRepository;

    public TestimonialService(TestimonialRepository testimonialRepository, UserRepository userRepository, ApprenantRepository apprenantRepository) {
        this.testimonialRepository = testimonialRepository;
        this.userRepository = userRepository;
        this.apprenantRepository = apprenantRepository;
    }

    @Transactional
    public CResponse<TestimonialResponse> addTestimonial(TestimonialRequest request, User currentUser) {
        if (currentUser == null) {
            return CResponse.error("User not authenticated"); 
        }
        
        // Vérifier que l'utilisateur est un apprenant
        Optional<Apprenant> apprenantOptional = apprenantRepository.findByUserId(currentUser.getId());
        if (apprenantOptional.isEmpty()) {
            return CResponse.error("Seuls les apprenants peuvent créer des témoignages. Veuillez créer votre profil apprenant d'abord.");
        }
        
        Apprenant apprenant = apprenantOptional.get();
        if (!apprenant.isActivate()) {
            return CResponse.error("Votre profil apprenant n'est pas actif. Veuillez contacter l'administrateur.");
        }
        
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return CResponse.error("Le contenu du témoignage ne peut pas être vide.");
        }

        Testimonial testimonial = new Testimonial();
        testimonial.setContent(request.getContent());
        testimonial.setUser(currentUser); 

        Testimonial savedTestimonial = testimonialRepository.save(testimonial);

        TestimonialResponse.UserInfo userInfo = new TestimonialResponse.UserInfo(
                currentUser.getId(),
                currentUser.getFullName(), 
                currentUser.getEmail()
        );

        TestimonialResponse response = new TestimonialResponse(
                savedTestimonial.getId(),
                savedTestimonial.getContent(),
                userInfo,
                savedTestimonial.getCreatedAt()
        );

        return CResponse.success(response, "Testimonial submitted successfully");
    }

    public CResponse<List<TestimonialResponse>> getAllTestimonials() {
        // Récupérer uniquement les témoignages actifs (activate = true)
        List<Testimonial> testimonials = testimonialRepository.findAll()
            .stream()
            .filter(Testimonial::isActivate) // Filtrer les témoignages actifs
            .collect(Collectors.toList());
        List<TestimonialResponse> responses = testimonials.stream().map(this::mapToTestimonialResponse).collect(Collectors.toList());
        return CResponse.success(responses, "Testimonials fetched successfully");
    }

    public CResponse<List<TestimonialResponse>> getTestimonialsByUser(Long userId) {
        List<Testimonial> testimonials = testimonialRepository.findByUserId(userId); 
        List<TestimonialResponse> responses = testimonials.stream().map(this::mapToTestimonialResponse).collect(Collectors.toList());
        return CResponse.success(responses, "Testimonials by user fetched successfully");
    }

    private TestimonialResponse mapToTestimonialResponse(Testimonial testimonial) {
        TestimonialResponse.UserInfo userInfo = null;
        if (testimonial.getUser() != null) {
            userInfo = new TestimonialResponse.UserInfo(
                    testimonial.getUser().getId(),
                    testimonial.getUser().getFullName(),
                    testimonial.getUser().getEmail()
            );
        }
        return new TestimonialResponse(
                testimonial.getId(),
                testimonial.getContent(),
                userInfo,
                testimonial.getCreatedAt()
        );
    }
}
