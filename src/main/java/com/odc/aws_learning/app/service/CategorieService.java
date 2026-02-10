package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Categorie;
import com.odc.aws_learning.app.repository.CategorieRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;


    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }



    public CResponse<?> getAllCategories() {
        List<Categorie> categories = categorieRepository.findAll();
        return CResponse.success(categories);
    }

    public Categorie getCategoryById(Long id) {
        return categorieRepository.findById(id).orElse(null);
    }

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public void updateCategorie( Categorie categorie) {
        Categorie existingCategory = getCategoryById(categorie.getId());
        if (existingCategory != null) {
            existingCategory.setTitle(categorie.getTitle());
            existingCategory.setDescription(categorie.getDescription());
            categorieRepository.save(existingCategory);
        }

    }

    public boolean deleteCategorie(Long id) {
        Categorie existingCategory = getCategoryById(id);
        if (existingCategory != null) {
            categorieRepository.delete(existingCategory);
        }
        return false;
    }
    
    /**
     * Compte le nombre de cours associés à une catégorie
     */
    public long getCoursesCountByCategoryId(Long categoryId) {
        return categorieRepository.countCoursesByCategoryId(categoryId);
    }
}
