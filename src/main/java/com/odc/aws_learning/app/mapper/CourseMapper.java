package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.CourseDto;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Module;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ModuleMapper.class, InstructorMapper.class})
public interface CourseMapper {

    @Mapping(target = "category", source = "course", qualifiedByName = "getCategoryTitle")
    @Mapping(target = "instructor", ignore = true) // Set by service
    @Mapping(target = "curriculum", source = "modules")
    @Mapping(target = "duration", source = "duration", qualifiedByName = "formatDuration")
    @Mapping(target = "imageUrl", source = "imagePath")
    @Mapping(target = "lastUpdated", source = "lastModifiedAt", qualifiedByName = "formatLastUpdated")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "certificationMode", source = "certificationMode")
    @Mapping(target = "rejectionReason", source = "rejectionReason")
    // Mappings for calculated fields (rating, reviewCount, enrolledCount) will be handled in the service
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "enrolledCount", ignore = true)
    CourseDto toDto(Courses course);

    List<CourseDto> toDtoList(List<Courses> courses);

    @Named("formatDuration")
    default String formatDuration(Integer durationInSeconds) {
        if (durationInSeconds == null || durationInSeconds <= 0) {
            return "0h 0min";
        }
        long minutes = durationInSeconds / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        return String.format("%dh %dmin", hours, minutes);
    }
    
    @Named("calculateCourseDuration")
    default String calculateCourseDuration(List<Module> modules) {
        if (modules == null || modules.isEmpty()) {
            return "0h 0min";
        }
        long totalSeconds = modules.stream()
                .flatMap(module -> module.getLessons().stream())
                .mapToLong(com.odc.aws_learning.app.entity.Lesson::getDuration)
                .sum();

        long minutes = totalSeconds / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        return String.format("%dh %dmin", hours, minutes);
    }

    @Named("formatLastUpdated")
    default String formatLastUpdated(java.time.LocalDateTime lastModifiedAt) {
        if (lastModifiedAt == null) {
            return null;
        }
        // Example format, adjust as needed by frontend
        return lastModifiedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    @Named("getCategoryTitle")
    default String getCategoryTitle(Courses course) {
        if (course == null || course.getCategorie() == null) {
            System.out.println("⚠️ [CourseMapper] Course ou Categorie est null");
            return "Non catégorisé";
        }

        System.out.println("🔍 [CourseMapper] getCategoryTitle pour cours ID: " + course.getId() + ", titre: " + course.getTitle());

        try {
            com.odc.aws_learning.app.entity.Categorie categorie = course.getCategorie();
            if (categorie instanceof HibernateProxy) {
                System.out.println("🔍 [CourseMapper] Catégorie est un proxy, initialisation...");
                try {
                    Hibernate.initialize(categorie);
                    System.out.println("✅ [CourseMapper] Catégorie proxy initialisé");
                } catch (Exception e) {
                    System.err.println("❌ [CourseMapper] Erreur initialisation catégorie: " + e.getMessage());
                    return "Non catégorisé";
                }
            }
            String title = categorie.getTitle();
            if (title != null && !title.trim().isEmpty()) {
                System.out.println("✅ [CourseMapper] Catégorie trouvée directement: " + title);
                return title;
            } else {
                System.out.println("⚠️ [CourseMapper] Titre catégorie vide ou null");
            }
        } catch (Exception e) {
            System.err.println("❌ [CourseMapper] Erreur accès catégorie: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("⚠️ [CourseMapper] Aucune catégorie trouvée, retour 'Non catégorisé'");
        return "Non catégorisé";
    }
}
