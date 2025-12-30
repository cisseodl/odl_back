package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Cohorte;
import com.odc.aws_learning.app.repository.CohorteRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import io.jsonwebtoken.io.IOException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CohorteService {

    private final CohorteRepository cohorteRepository;

    public CohorteService(CohorteRepository cohorteRepository) {
        this.cohorteRepository = cohorteRepository;
    }

    public CResponse<?> getAllCohortes() {
        List<Cohorte> cohortes = cohorteRepository.findAll();
        return CResponse.success(cohortes, "Cohortes");
    }

    public CResponse<?> getCohorteById(Long id) {
        Optional<Cohorte> cohorteOptional = cohorteRepository.findById(id);
        if (cohorteOptional.isPresent()) {
            return CResponse.success(cohorteOptional.get());
        }
        return CResponse.error("Cette cohorte est introuvable");
    }

    public CResponse<?> createCohorte(Cohorte cohorte) {
        try {
            Cohorte cohorte1 = cohorteRepository.save(cohorte);
            return CResponse.success(cohorte1, "Cohorte enregistrée avec succès");
        }catch (Exception e){
            System.out.println(e);
            return CResponse.error("Erreur d'enregistrement");

        }

    }

    public CResponse<?> updateCohorte(Cohorte updatedCohorte) {
        try {
            Optional<Cohorte> cohorteOptional = cohorteRepository.findById(updatedCohorte.getId());
            if (cohorteOptional.isPresent()) {
                cohorteOptional.get().setNom(updatedCohorte.getNom());
                cohorteOptional.get().setDescription(updatedCohorte.getDescription());
                cohorteOptional.get().setDateDebut(updatedCohorte.getDateDebut());
                cohorteOptional.get().setDateFin(updatedCohorte.getDateFin());
                 cohorteRepository.save(cohorteOptional.get());
        }
        }catch (IOException e){
            System.out.println(e);
            return CResponse.error("Erreur de modification");
        }
        return null;
    }

    public CResponse<?> deleteCohorte(Long id) {
        Cohorte existingCohorte = cohorteRepository.findById(id).orElse(null);
        try {
            if (existingCohorte != null) {
                cohorteRepository.delete(existingCohorte);
                return CResponse.error("supprimer avec succès");

            }
        }catch (IOException e){
            return CResponse.error("erreur de suppression");
        }

        return null;
    }
}
