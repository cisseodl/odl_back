package com.odc.aws_learning.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odc.aws_learning.app.entity.Rubrique;
import com.odc.aws_learning.app.service.RubriqueService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/v1/rubriques")
public class RubriqueController {

    private final RubriqueService rubriqueService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;


    public RubriqueController(RubriqueService rubriqueService, ObjectMapper objectMapper, UserRepository userRepository) {
        this.rubriqueService = rubriqueService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }


    @GetMapping("/read")
    public ResponseEntity<CResponse<List<Rubrique>>> getAllRubriques() {
        List<Rubrique> rubriques = rubriqueService.getAllRubriques();
        return ResponseEntity.ok(CResponse.success(rubriques, "Liste des rubriques récupérée avec succès."));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<CResponse<Rubrique>> getRubriqueById(@PathVariable Long id) {
        return rubriqueService.getRubriqueById(id)
                .map(rubrique -> ResponseEntity.ok(CResponse.success(rubrique)))
                .orElse(new ResponseEntity<>(CResponse.error("Rubrique non trouvée"), HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<Rubrique>> createRubrique(
            @RequestPart("rubrique") String rubriqueJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            Principal principal) {
        try {
            Rubrique rubrique = objectMapper.readValue(rubriqueJson, Rubrique.class);
            Rubrique savedRubrique = rubriqueService.saveRubrique(rubrique, imageFile, principal.getName());
            return new ResponseEntity<>(CResponse.success(savedRubrique, "Rubrique créée avec succès"), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(CResponse.error("Erreur lors de la désérialisation de la rubrique ou du traitement de l'image: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(CResponse.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<Rubrique>> updateRubrique(
            @PathVariable Long id,
            @RequestPart("rubrique") String rubriqueJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Rubrique rubriqueDetails = objectMapper.readValue(rubriqueJson, Rubrique.class);
            Rubrique updatedRubrique = rubriqueService.updateRubrique(id, rubriqueDetails, imageFile);
            return ResponseEntity.ok(CResponse.success(updatedRubrique, "Rubrique mise à jour avec succès"));
        } catch (IOException e) {
            return new ResponseEntity<>(CResponse.error("Erreur lors de la désérialisation de la rubrique ou du traitement de l'image: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(CResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<Void>> deleteRubrique(@PathVariable Long id) {
        try {
            rubriqueService.deleteRubrique(id);
            return ResponseEntity.ok(CResponse.success(null, "Rubrique supprimée avec succès"));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(CResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
