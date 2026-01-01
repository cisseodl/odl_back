package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.ModuleDto;
import com.odc.aws_learning.app.entity.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface ModuleMapper {

    @Mapping(target = "duration", source = "lessons", qualifiedByName = "calculateModuleDuration")
    ModuleDto toDto(Module module);

    List<ModuleDto> toDtoList(List<Module> modules);

    @Named("calculateModuleDuration")
    default String calculateModuleDuration(List<com.odc.aws_learning.app.entity.Lesson> lessons) {
        if (lessons == null || lessons.isEmpty()) {
            return "0h 0min";
        }
        long totalSeconds = lessons.stream()
                .mapToLong(com.odc.aws_learning.app.entity.Lesson::getDuration)
                .sum();

        long minutes = totalSeconds / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        return String.format("%dh %dmin", hours, minutes);
    }
}
