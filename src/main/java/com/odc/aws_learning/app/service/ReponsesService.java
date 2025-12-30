package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Reponses;
import com.odc.aws_learning.app.repository.ReponsesRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReponsesService {
    private final ReponsesRepository reponsesRepository;

    public ReponsesService(ReponsesRepository reponsesRepository){
        this.reponsesRepository = reponsesRepository;
    }
    public CResponse<?> saveReponses(Reponses reponses){
        try {
           Reponses reponses1 = reponsesRepository.save(reponses);
            return CResponse.success(reponses1,"Reponses enregistré avec succès");
        }
        catch (Exception e){
            return CResponse.error("Erreur d'enregistrement");

        }
    }
    public CResponse<?> getAll(){
        try {
            List<Reponses> reponses = reponsesRepository.findAll();
            return CResponse.success(reponses, "Les Reponses");

        }catch (Exception e){
            return CResponse.error("Erreur de récupération");

        }
    }
}