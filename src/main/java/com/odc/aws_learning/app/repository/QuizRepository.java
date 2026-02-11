package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCourseId(Long courseId);
    List<Quiz> findByCourseIdAndActivateTrue(Long courseId);

    /** Récupère les quiz du cours avec la leçon chargée (pour exposer lessonId côté apprenant). */
    @Query("SELECT DISTINCT q FROM Quiz q LEFT JOIN FETCH q.lesson WHERE q.course.id = :courseId AND q.activate = true")
    List<Quiz> findByCourseIdAndActivateTrueWithLesson(@Param("courseId") Long courseId);

    /**
     * Quiz « jouables » : actifs et ayant au moins une question avec au moins une réponse.
     * Utilisé pour les Prochaines Étapes du dashboard et pour ne pas proposer des quiz incomplets.
     */
    @Query("SELECT DISTINCT q FROM Quiz q WHERE q.course.id = :courseId AND q.activate = true " +
           "AND EXISTS (SELECT 1 FROM QuizQuestion qq JOIN qq.reponses r WHERE qq.quiz = q)")
    List<Quiz> findPlayableByCourseId(@Param("courseId") Long courseId);

    /**
     * Même critère que findPlayableByCourseId mais avec la leçon chargée (pour exposer lessonId).
     * Utilisé pour la liste des quiz d'un cours (Activités associées, etc.).
     */
    @Query("SELECT DISTINCT q FROM Quiz q LEFT JOIN FETCH q.lesson WHERE q.course.id = :courseId AND q.activate = true " +
           "AND EXISTS (SELECT 1 FROM QuizQuestion qq JOIN qq.reponses r WHERE qq.quiz = q)")
    List<Quiz> findPlayableByCourseIdWithLesson(@Param("courseId") Long courseId);

    /**
     * Charge un quiz avec ses questions (les réponses sont chargées en lazy dans la même transaction).
     */
    @Query("SELECT DISTINCT q FROM Quiz q LEFT JOIN FETCH q.lesson LEFT JOIN FETCH q.questions WHERE q.id = :quizId")
    java.util.Optional<Quiz> findByIdWithQuestionsAndReponses(@Param("quizId") Long quizId);
}
