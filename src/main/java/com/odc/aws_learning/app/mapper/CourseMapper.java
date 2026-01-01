package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.CourseDto;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ModuleMapper.class, InstructorMapper.class})
public interface CourseMapper {

    @Mapping(target = "category", source = "categorie.title")
    @Mapping(target = "instructor", ignore = true) // Set by service
    @Mapping(target = "curriculum", source = "modules")
    @Mapping(target = "duration", source = "modules", qualifiedByName = "calculateCourseDuration")
    @Mapping(target = "imageUrl", source = "imagePath")
    @Mapping(target = "lastUpdated", source = "lastModifiedAt", qualifiedByName = "formatLastUpdated")
    // Mappings for calculated fields (rating, reviewCount, enrolledCount) will be handled in the service
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "enrolledCount", ignore = true)
    CourseDto toDto(Courses course);

    List<CourseDto> toDtoList(List<Courses> courses);

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
}
