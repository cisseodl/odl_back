package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.CourseDto;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Module;
import com.odc.aws_learning.app.entity.Formation;
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
        // Nouvelle hiérarchie : Formation -> Cours
        // Si formation existe, utiliser formation.getCategorie().getTitle()
        // Sinon, utiliser categorie.getTitle() (ancien système)
        try {
            Formation formation = course.getFormation();
            // Vérifier si formation n'est pas null et n'est pas un proxy non initialisé avec ID 0
            if (formation != null) {
                // Vérifier si c'est un proxy Hibernate non initialisé
                if (formation instanceof HibernateProxy) {
                    HibernateProxy proxy = (HibernateProxy) formation;
                    Object identifier = proxy.getHibernateLazyInitializer().getIdentifier();
                    // Si l'ID est 0 ou null, c'est probablement un proxy non valide
                    if (identifier == null || (identifier instanceof Number && ((Number) identifier).longValue() == 0)) {
                        // Ignorer la formation et utiliser la catégorie directe
                        formation = null;
                    } else if (!Hibernate.isInitialized(formation)) {
                        // Essayer d'initialiser le proxy
                        try {
                            Hibernate.initialize(formation);
                        } catch (Exception e) {
                            // Si l'initialisation échoue, utiliser la catégorie directe
                            formation = null;
                        }
                    }
                }
                
                // Si formation est valide et a une catégorie, l'utiliser
                if (formation != null) {
                    try {
                        // Vérifier si la catégorie est accessible
                        if (Hibernate.isInitialized(formation) || !(formation instanceof HibernateProxy)) {
                            if (formation.getCategorie() != null) {
                                return formation.getCategorie().getTitle();
                            }
                        }
                    } catch (Exception e) {
                        // Si l'accès à la catégorie échoue, continuer avec la catégorie directe
                    }
                }
            }
        } catch (Exception e) {
            // En cas d'erreur, continuer avec la catégorie directe
        }
        
        // Utiliser la catégorie directe (ancien système)
        try {
            if (course.getCategorie() != null) {
                return course.getCategorie().getTitle();
            }
        } catch (Exception e) {
            // Ignorer les erreurs d'accès à la catégorie
        }
        
        return null;
    }
}
