package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.InstructorDto;
import com.odc.aws_learning.app.entity.InstructorProfile;
import com.odc.aws_learning.auth.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-09T19:14:20+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class InstructorMapperImpl implements InstructorMapper {

    @Override
    public InstructorDto toDto(User user, InstructorProfile instructorProfile) {
        if ( user == null && instructorProfile == null ) {
            return null;
        }

        InstructorDto.InstructorDtoBuilder instructorDto = InstructorDto.builder();

        if ( user != null ) {
            instructorDto.id( user.getId() );
            instructorDto.name( user.getFullName() );
            instructorDto.avatar( user.getAvatar() );
        }
        if ( instructorProfile != null ) {
            instructorDto.title( instructorProfile.getTitle() );
            instructorDto.bio( instructorProfile.getBio() );
        }

        return instructorDto.build();
    }
}
