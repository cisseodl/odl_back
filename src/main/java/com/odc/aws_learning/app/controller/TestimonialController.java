import com.odc.aws_learning.app.dto.TestimonialRequest;
import com.odc.aws_learning.app.dto.TestimonialResponse;
import com.odc.aws_learning.app.service.TestimonialService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Nouvelle importation
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/testimonials")
@RestController
public class TestimonialController {

    private final TestimonialService testimonialService;

    public TestimonialController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()") // Annotation ajoutée
    public ResponseEntity<CResponse<TestimonialResponse>> addTestimonial(
            @Valid @RequestBody TestimonialRequest request,
            @AuthenticationPrincipal User currentUser) {
        // Le contrôle `if (currentUser == null)` est maintenu pour la robustesse,
        // mais @PreAuthorize le gérera généralement avant d'atteindre ce point.
        if (currentUser == null) {
            return new ResponseEntity<>(CResponse.error("User not authenticated"), HttpStatus.UNAUTHORIZED);
        }
        CResponse<TestimonialResponse> response = testimonialService.addTestimonial(request, currentUser);
        return new ResponseEntity<>(response, response.getStatus().equals("success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<CResponse<List<TestimonialResponse>>> getAllTestimonials() {
        CResponse<List<TestimonialResponse>> response = testimonialService.getAllTestimonials();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CResponse<List<TestimonialResponse>>> getTestimonialsByUser(@PathVariable Long userId) {
        CResponse<List<TestimonialResponse>> response = testimonialService.getTestimonialsByUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
