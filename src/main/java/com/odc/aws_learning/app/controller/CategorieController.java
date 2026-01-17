package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.CategorieRequest;
import com.odc.aws_learning.app.entity.Categorie;
import com.odc.aws_learning.app.service.CategorieService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@RequestMapping("/api/categories")
@RestController
public class CategorieController {

    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping("/read")
    // Endpoint public pour permettre aux apprenants de consulter les catégories sans authentification
    public CResponse<?> getAllCategories() {
        return categorieService.getAllCategories();
    }

    @GetMapping("/read/{id}")
    // Endpoint public pour permettre aux apprenants de consulter les catégories sans authentification
    public ResponseEntity<Categorie> getCategoryById(@PathVariable Long id) {
        Categorie categorie = categorieService.getCategoryById(id);
        if (categorie != null) {
            return ResponseEntity.ok().body(categorie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<Categorie> createCategory(@org.springframework.web.bind.annotation.RequestBody CategorieRequest request) {
        Categorie categorie = new Categorie();
        categorie.setTitle(request.getTitle());
        categorie.setDescription(request.getDescription());
        Categorie createdCategory = categorieService.createCategorie(categorie);
        return CResponse.success(createdCategory, "Categorie enregistrée");
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<String> updateCategory(@org.springframework.web.bind.annotation.RequestBody CategorieRequest request) {
        try {
            Categorie existingCategorie = categorieService.getCategoryById(request.getId()); // Supposons que CategorieRequest a un getId()
            if (existingCategorie != null) {
                existingCategorie.setTitle(request.getTitle());
                existingCategorie.setDescription(request.getDescription());
                categorieService.updateCategorie(existingCategorie);
                return ResponseEntity.ok().body("La catégorie a été mise à jour avec succès.");
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la modification.");
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        boolean deleted = categorieService.deleteCategorie(id);
        if (deleted) {
            return ResponseEntity.ok().body("La catégorie a été supprimée avec succès.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
