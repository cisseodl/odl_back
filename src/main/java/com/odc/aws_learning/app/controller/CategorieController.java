package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Categorie;
import com.odc.aws_learning.app.service.CategorieService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/categories")
@RestController
public class CategorieController {

    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getAllCategories() {
        return categorieService.getAllCategories();
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public ResponseEntity<Categorie> getCategoryById(@PathVariable Long id) {
        Categorie categorie = categorieService.getCategoryById(id);
        if (categorie != null) {
            return ResponseEntity.ok().body(categorie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<Categorie> createCategory(@RequestBody Categorie categorie) {
        Categorie createdCategory = categorieService.createCategorie(categorie);
        return CResponse.success(createdCategory, "Categorie enregistrée");
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateCategory(@RequestBody Categorie categorie) {
        try {
            if (categorie != null) {
                categorieService.updateCategorie(categorie);
                return ResponseEntity.ok().body("Le cours a été mis à jour avec succès.");
                //categorieService.updateCategorie(categorie);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la modification.");
        }



    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        boolean deleted = categorieService.deleteCategorie(id);
        if (deleted) {
            return ResponseEntity.ok().body("La catégorie a été supprimée avec succès.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
