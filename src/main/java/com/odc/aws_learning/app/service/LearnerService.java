package com.odc.aws_learning.app.service;


import com.odc.aws_learning.app.entity.Module;

import com.odc.aws_learning.app.repository.LearnerModuleRepository;
import com.odc.aws_learning.app.entity.LearnerModule;
import com.odc.aws_learning.app.repository.ModuleRepository;
import com.odc.aws_learning.app.wrapper.ValidateModule;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LearnerService {


    private final UserRepository userRepository;
    private final LearnerModuleRepository learnerModuleRepository;
    private final ModuleRepository moduleRepository;

    public LearnerService(UserRepository userRepository, LearnerModuleRepository learnerModuleRepository, ModuleRepository moduleRepository) {
        this.userRepository = userRepository;
        this.learnerModuleRepository = learnerModuleRepository;
        this.moduleRepository = moduleRepository;
    }

    public CResponse<?> saveLearner(ValidateModule validateModule){
        try {
            // Vérifier si l'utilisateur existe
            Optional<User> optionalUser = userRepository.findById(validateModule.getUserId());
            if(optionalUser.isPresent()){
                Optional<Module> optionalModule = moduleRepository.findByActivateAndIdAndCourseId(true, validateModule.getModuleId(), validateModule.getCoursId());
                if (optionalModule.isPresent()) {
                    LearnerModule learnerModule = new LearnerModule();
                    learnerModule.setLearner(optionalUser.get());
                    learnerModule.setModule(optionalModule.get());
                    LearnerModule learnerModule1 = learnerModuleRepository.save(learnerModule);
                    return CResponse.success(learnerModule1,"Enregistrer avec succès");
                }else {
                    return CResponse.error("Module n'existe pas ");
                }
            } else {
                return CResponse.error("Users n'existe pas");
            }
        }catch (Exception e){
            return CResponse.error("");
        }

    }
}
