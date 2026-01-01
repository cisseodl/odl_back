package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.InstructorDto;
import com.odc.aws_learning.app.entity.InstructorProfile;
import com.odc.aws_learning.auth.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.fullName")
    @Mapping(target = "avatar", source = "user.avatar")
    @Mapping(target = "title", source = "instructorProfile.title")
    @Mapping(target = "bio", source = "instructorProfile.bio")
    // Calculated fields will be handled in the service or by specific methods
    @Mapping(target = "studentCount", ignore = true)
    @Mapping(target = "courseCount", ignore = true)
    @Mapping(target = "rating", ignore = true)
    InstructorDto toDto(User user, InstructorProfile instructorProfile);
}
