package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.UploadLink;
import com.odc.aws_learning.app.entity.Categorie;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.DetailsCourse;
import com.odc.aws_learning.app.repository.CategorieRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoursesService {
    private final CoursesRepository coursesRepository;
    private final CategorieRepository categorieRepository;
    private final DetailsCourseRepository detailsCourseRepository;

    public CoursesService(CoursesRepository coursesRepository, 
                         CategorieRepository categorieRepository,
                         DetailsCourseRepository detailsCourseRepository) {
        this.coursesRepository = coursesRepository;
        this.categorieRepository = categorieRepository;
        this.detailsCourseRepository = detailsCourseRepository;
    }

    // Chemin vers le dossier de téléchargement configuré dans application.properties




    /*  public CResponse<?> saveCourses(Courses courses) {
        try {
            Courses courses1 = coursesRepository.save(courses);
            return CResponse.success(courses1, "Cours enregistré");
        } catch (Exception e) {
            return CResponse.error("Erreur d'enregistrement");
        }
    }
   public Courses saveCourses(String title, String description, String status, MultipartFile image) throws IOException {
        String imagePath = StringUtils.cleanPath(image.getOriginalFilename());
        Courses cours = new Courses(title, description, status, imagePath, image.getBytes());
        return coursesRepository.save(cours);
    }*/

    // Ajouter un nouveau cours avec une image
    public CResponse<?> addCourseWithImage(Courses courses, MultipartFile image, Long catId) throws IOException {
        Optional<Categorie> categorieOptional = categorieRepository.findById(catId);
        categorieOptional.ifPresent(courses::setCategorie);
        // Générer un nom de fichier unique pour éviter les conflits de noms
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        // Construire le chemin complet du fichier
        String filePath = UploadLink.DOWNLOAD_LINK + File.separator + "cours/" + fileName;

        // Écrire les données de l'image dans le fichier
        Files.write(Paths.get(filePath), image.getBytes());

        // Créer un nouvel objet Course avec les détails fournis
        /*Courses course = new Courses();
        course.setTitle(title);
        course.setDescription(description);*/

        // Enregistrer le chemin de l'image
        courses.setImagePath(fileName);

        // Enregistrer le cours dans la base de données
       Courses coursesSaved = coursesRepository.save(courses);

       return CResponse.success(coursesSaved, "Cours enregistré avec succès");
    }

    // Récupérer tous les cours
    public List<Courses> getAllCourses() {
        return coursesRepository.findAll();
    }

    // Récupérer un cours par son ID
   public CResponse<?> getCourseById(Long id) {
       try {
           Optional<Courses> coursesOptional = coursesRepository.findById(id);
           if (coursesOptional.isPresent()) {
               return CResponse.success(coursesOptional.get());
           }
           return CResponse.error("Ce cours est introuvable");
       } catch (Exception e) {
           return CResponse.error("Erreur de récupération");
       }
    }

    // Mettre à jour un cours existant
    public void updateCourse(Courses courses, MultipartFile image) throws IOException {
       Optional<Courses> coursesOptional = coursesRepository.findById(courses.getId());
        if (coursesOptional.isPresent()) {
            // Si une nouvelle image est fournie, la mettre à jour
            if (!image.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                String filePath = UploadLink.DOWNLOAD_LINK + File.separator + fileName;
                Files.write(Paths.get(filePath), image.getBytes());
                coursesOptional.get().setImagePath(fileName);
            }
            // Mettre à jour les autres informations du cours
            coursesOptional.get().setTitle(courses.getTitle());
            coursesOptional.get().setDescription(courses.getDescription());

            // Enregistrer les modifications dans la base de données
            coursesRepository.save(coursesOptional.get());
        }
    }

    // Supprimer un cours par son ID
    public boolean deleteCourse(Long id) {
        Optional<Courses> coursesOptional = coursesRepository.findById(id);
        if (coursesOptional.isPresent()) {
            // Supprimer le fichier image associé
            String filePath = UploadLink.DOWNLOAD_LINK + File.separator + "cours/" + coursesOptional.get().getImagePath();
            File file = new File(filePath);
            file.delete();
            // Supprimer le cours de la base de données
            coursesRepository.delete(coursesOptional.get());
            return true;
        }
        return false;
    }
    /*
    }

    public Courses updateCours(Long id, String title, String description, String status, MultipartFile image) throws IOException {
        Optional<Courses> optionalCours = coursesRepository.findById(id);
        if (optionalCours.isPresent()) {
            Courses cours = optionalCours.get();
            cours.setTitle(title);
            cours.setDescription(description);
            cours.setStatus(status);
            if (image != null && !image.isEmpty()) {
                cours.setImagePath(StringUtils.cleanPath(image.getOriginalFilename()));
                cours.setImage(image.getBytes());
            }
            return coursesRepository.save(cours);
        } else {
            return null;
        }
    }

    public void deleteCours(Long id) {
            coursesRepository.deleteById(id);
    }
    public CResponse<?> getAll() {
        try {
            List<Courses> courses = coursesRepository.findAll();
            return CResponse.success(courses, "Les cours");
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }*/

    public CResponse<?> getCoursesByPage(int page, int size) {
        try {
            Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable paging = PageRequest.of(page, size, defaultSort);
            Page<Courses> courses = coursesRepository.findAllByActivate(true, paging);
            return CResponse.success(courses);
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }

    public List<Courses> getCoursesByCategory(Long catId) {
        return coursesRepository.findByCategorieId(catId);
    }

    /**
     * Inscrit un utilisateur à un cours (inscription gratuite directe).
     * Crée une entrée dans DetailsCourse si l'utilisateur n'est pas déjà inscrit.
     * 
     * @param user L'utilisateur à inscrire
     * @param courseId L'ID du cours
     * @return Réponse indiquant le succès ou l'échec de l'inscription
     */
    public CResponse<?> enrollUserInCourse(User user, Long courseId) {
        try {
            // Vérifier que le cours existe
            Optional<Courses> courseOptional = coursesRepository.findById(courseId);
            if (courseOptional.isEmpty()) {
                return CResponse.error("Cours non trouvé avec l'ID: " + courseId);
            }

            Courses course = courseOptional.get();

            // Vérifier si l'utilisateur est déjà inscrit
            Optional<DetailsCourse> existingEnrollment = detailsCourseRepository
                    .findByCourseIdAndLearnerId(courseId, user.getId());

            if (existingEnrollment.isPresent()) {
                return CResponse.error("Vous êtes déjà inscrit à ce cours");
            }

            // Créer l'inscription
            DetailsCourse detailsCourse = new DetailsCourse();
            detailsCourse.setCourse(course);
            detailsCourse.setLearner(user);
            detailsCourse.setActivate(true);
            detailsCourseRepository.save(detailsCourse);

            return CResponse.success(detailsCourse, "Inscription au cours réussie");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de l'inscription: " + e.getMessage());
        }
    }
}
