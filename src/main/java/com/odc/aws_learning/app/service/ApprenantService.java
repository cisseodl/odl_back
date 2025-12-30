package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.entity.Cohorte;
import com.odc.aws_learning.app.repository.ApprenantRepository;
import com.odc.aws_learning.app.repository.CohorteRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApprenantService {
    private final ApprenantRepository apprenantRepository;
    private final CohorteRepository cohorteRepository;

    public ApprenantService(ApprenantRepository apprenantRepository, CohorteRepository cohorteRepository) {
        this.apprenantRepository = apprenantRepository;
        this.cohorteRepository = cohorteRepository;
    }

    public CResponse<?> saveApprenant(Apprenant apprenant) {
        try {
            Apprenant apprenant1 = apprenantRepository.save(apprenant);
            return CResponse.success(apprenant1,"Apprenant enregistré avec succès");
        }
        catch (Exception e){
            return CResponse.error("Erreur d'enregistrement");
        }
    }
    public CResponse<?> getAll(){
        try {
            List<Apprenant> apprenants = apprenantRepository.findAll();
            return CResponse.success(apprenants, "Les Apprenants");

        }catch (Exception e){
            return CResponse.error("Erreur de récupération");

        }
    }

    public CResponse<?> getByCohorte(Long cohorteId, int page, int size) {
        try {
            Optional<Cohorte> cohorteOptional = cohorteRepository.findById(cohorteId);
            if (cohorteOptional.isPresent()) {
                Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdAt");
                Pageable paging = PageRequest.of(page, size, defaultSort);
                Page<Apprenant> apprenants = apprenantRepository.findAllByActivateAndCohorteId(true, cohorteId, paging);
                return CResponse.success(apprenants, "Les apprenant de " + cohorteOptional.get().getNom());
            }
            return CResponse.error("Cette cohorte n'existe pas");
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }
}


