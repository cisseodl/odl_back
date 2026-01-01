package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.LessonDto;
import com.odc.aws_learning.app.dto.ModuleDto;
import com.odc.aws_learning.app.entity.Lesson;
import com.odc.aws_learning.app.entity.Module;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-01T17:12:30+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class ModuleMapperImpl implements ModuleMapper {

    @Autowired
    private LessonMapper lessonMapper;

    @Override
    public ModuleDto toDto(Module module) {
        if ( module == null ) {
            return null;
        }

        ModuleDto.ModuleDtoBuilder moduleDto = ModuleDto.builder();

        moduleDto.duration( calculateModuleDuration( module.getLessons() ) );
        moduleDto.id( module.getId() );
        moduleDto.title( module.getTitle() );
        moduleDto.lessons( lessonListToLessonDtoList( module.getLessons() ) );

        return moduleDto.build();
    }

    @Override
    public List<ModuleDto> toDtoList(List<Module> modules) {
        if ( modules == null ) {
            return null;
        }

        List<ModuleDto> list = new ArrayList<ModuleDto>( modules.size() );
        for ( Module module : modules ) {
            list.add( toDto( module ) );
        }

        return list;
    }

    protected List<LessonDto> lessonListToLessonDtoList(List<Lesson> list) {
        if ( list == null ) {
            return null;
        }

        List<LessonDto> list1 = new ArrayList<LessonDto>( list.size() );
        for ( Lesson lesson : list ) {
            list1.add( lessonMapper.toDto( lesson ) );
        }

        return list1;
    }
}
