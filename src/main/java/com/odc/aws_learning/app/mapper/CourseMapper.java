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
        if (course == null) {
            System.out.println("⚠️ [CourseMapper] Course is null");
            return "Non catégorisé";
        }
        
        System.out.println("🔍 [CourseMapper] getCategoryTitle pour cours ID: " + course.getId() + ", titre: " + course.getTitle());
        
        // Nouvelle hiérarchie : Formation -> Cours
        // Si formation existe, utiliser formation.getCategorie().getTitle()
        // Sinon, utiliser categorie.getTitle() (ancien système)
        try {
            Formation formation = course.getFormation();
            System.out.println("🔍 [CourseMapper] Formation: " + (formation != null ? "présente (ID: " + (formation instanceof HibernateProxy ? 
                ((HibernateProxy) formation).getHibernateLazyInitializer().getIdentifier() : formation.getId()) + ")" : "null"));
            
            // Vérifier si formation n'est pas null et n'est pas un proxy non initialisé avec ID 0
            if (formation != null) {
                // Vérifier si c'est un proxy Hibernate non initialisé
                if (formation instanceof HibernateProxy) {
                    HibernateProxy proxy = (HibernateProxy) formation;
                    Object identifier = proxy.getHibernateLazyInitializer().getIdentifier();
                    System.out.println("🔍 [CourseMapper] Formation est un proxy, ID: " + identifier);
                    // Si l'ID est 0 ou null, c'est probablement un proxy non valide
                    if (identifier == null || (identifier instanceof Number && ((Number) identifier).longValue() == 0)) {
                        System.out.println("⚠️ [CourseMapper] Formation proxy invalide (ID 0 ou null), utilisation catégorie directe");
                        // Ignorer la formation et utiliser la catégorie directe
                        formation = null;
                    } else if (!Hibernate.isInitialized(formation)) {
                        System.out.println("🔍 [CourseMapper] Initialisation du proxy formation...");
                        // Essayer d'initialiser le proxy
                        try {
                            Hibernate.initialize(formation);
                            // Essayer aussi d'initialiser la catégorie de la formation
                            if (formation.getCategorie() != null && formation.getCategorie() instanceof HibernateProxy) {
                                Hibernate.initialize(formation.getCategorie());
                            }
                            System.out.println("✅ [CourseMapper] Formation proxy initialisé avec succès");
                        } catch (Exception e) {
                            System.err.println("❌ [CourseMapper] Erreur initialisation formation proxy: " + e.getMessage());
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
                            com.odc.aws_learning.app.entity.Categorie formationCategorie = formation.getCategorie();
                            System.out.println("🔍 [CourseMapper] Catégorie formation: " + (formationCategorie != null ? "présente" : "null"));
                            if (formationCategorie != null) {
                                // Vérifier si la catégorie est un proxy non initialisé
                                if (formationCategorie instanceof HibernateProxy) {
                                    try {
                                        Hibernate.initialize(formationCategorie);
                                    } catch (Exception e) {
                                        System.err.println("❌ [CourseMapper] Erreur initialisation catégorie formation: " + e.getMessage());
                                        // Si l'initialisation échoue, continuer avec la catégorie directe
                                        formationCategorie = null;
                                    }
                                }
                                if (formationCategorie != null && formationCategorie.getTitle() != null && !formationCategorie.getTitle().trim().isEmpty()) {
                                    System.out.println("✅ [CourseMapper] Catégorie trouvée via formation: " + formationCategorie.getTitle());
                                    return formationCategorie.getTitle();
                                } else {
                                    System.out.println("⚠️ [CourseMapper] Catégorie formation vide ou null");
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("❌ [CourseMapper] Erreur accès catégorie formation: " + e.getMessage());
                        // Si l'accès à la catégorie échoue, continuer avec la catégorie directe
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ [CourseMapper] Erreur générale formation: " + e.getMessage());
            e.printStackTrace();
            // En cas d'erreur, continuer avec la catégorie directe
        }
        
        // Utiliser la catégorie directe (ancien système)
        try {
            com.odc.aws_learning.app.entity.Categorie categorie = course.getCategorie();
            System.out.println("🔍 [CourseMapper] Catégorie directe: " + (categorie != null ? "présente" : "null"));
            if (categorie != null) {
                // Vérifier si la catégorie est un proxy non initialisé
                if (categorie instanceof HibernateProxy) {
                    System.out.println("🔍 [CourseMapper] Catégorie est un proxy, initialisation...");
                    try {
                        Hibernate.initialize(categorie);
                        System.out.println("✅ [CourseMapper] Catégorie proxy initialisé");
                    } catch (Exception e) {
                        System.err.println("❌ [CourseMapper] Erreur initialisation catégorie directe: " + e.getMessage());
                        // Si l'initialisation échoue, retourner "Non catégorisé"
                        return "Non catégorisé";
                    }
                }
                String title = categorie.getTitle();
                System.out.println("🔍 [CourseMapper] Titre catégorie directe: " + title);
                if (title != null && !title.trim().isEmpty()) {
                    System.out.println("✅ [CourseMapper] Catégorie trouvée directement: " + title);
                    return title;
                } else {
                    System.out.println("⚠️ [CourseMapper] Titre catégorie vide ou null");
                }
            } else {
                System.out.println("⚠️ [CourseMapper] Catégorie directe est null");
            }
        } catch (Exception e) {
            System.err.println("❌ [CourseMapper] Erreur accès catégorie directe: " + e.getMessage());
            e.printStackTrace();
            // Ignorer les erreurs d'accès à la catégorie
        }
        
        // Retourner "Non catégorisé" au lieu de null pour éviter les problèmes frontend
        System.out.println("⚠️ [CourseMapper] Aucune catégorie trouvée, retour 'Non catégorisé'");
        return "Non catégorisé";
    }
}
