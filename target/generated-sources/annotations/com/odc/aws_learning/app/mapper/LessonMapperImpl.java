package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.LessonDto;
import com.odc.aws_learning.app.entity.Lesson;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-01T17:12:30+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class LessonMapperImpl implements LessonMapper {

    @Override
    public LessonDto toDto(Lesson lesson) {
        if ( lesson == null ) {
            return null;
        }

        LessonDto.LessonDtoBuilder lessonDto = LessonDto.builder();

        lessonDto.duration( formatDuration( lesson.getDuration() ) );
        lessonDto.id( lesson.getId() );
        lessonDto.title( lesson.getTitle() );
        lessonDto.type( lesson.getType() );

        return lessonDto.build();
    }
}
