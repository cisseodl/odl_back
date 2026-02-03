package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.LessonDto;
import com.odc.aws_learning.app.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "duration", source = "duration", qualifiedByName = "formatDuration")
    @Mapping(target = "contentUrl", source = "contentUrl")
    LessonDto toDto(Lesson lesson);

    @Named("formatDuration")
    default String formatDuration(Integer durationInSeconds) {
        if (durationInSeconds == null || durationInSeconds <= 0) {
            return "0 min";
        }
        long minutes = durationInSeconds / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        if (hours > 0) {
            return String.format("%dh %dmin", hours, minutes);
        } else {
            return String.format("%d min", minutes);
        }
    }
}
