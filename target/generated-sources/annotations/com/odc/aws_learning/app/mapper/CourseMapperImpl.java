package com.odc.aws_learning.app.mapper;

import com.odc.aws_learning.app.dto.CourseDto;
import com.odc.aws_learning.app.entity.Categorie;
import com.odc.aws_learning.app.entity.Courses;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-01T17:12:30+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class CourseMapperImpl implements CourseMapper {

    @Autowired
    private ModuleMapper moduleMapper;

    @Override
    public CourseDto toDto(Courses course) {
        if ( course == null ) {
            return null;
        }

        CourseDto.CourseDtoBuilder courseDto = CourseDto.builder();

        courseDto.category( courseCategorieTitle( course ) );
        courseDto.curriculum( moduleMapper.toDtoList( course.getModules() ) );
        courseDto.duration( calculateCourseDuration( course.getModules() ) );
        courseDto.imageUrl( course.getImagePath() );
        courseDto.lastUpdated( formatLastUpdated( course.getLastModifiedAt() ) );
        courseDto.id( course.getId() );
        courseDto.title( course.getTitle() );
        courseDto.subtitle( course.getSubtitle() );
        courseDto.description( course.getDescription() );
        courseDto.level( course.getLevel() );
        courseDto.language( course.getLanguage() );
        if ( course.getBestseller() != null ) {
            courseDto.bestseller( course.getBestseller() );
        }
        Set<String> set = course.getObjectives();
        if ( set != null ) {
            courseDto.objectives( new LinkedHashSet<String>( set ) );
        }
        Set<String> set1 = course.getFeatures();
        if ( set1 != null ) {
            courseDto.features( new LinkedHashSet<String>( set1 ) );
        }

        return courseDto.build();
    }

    @Override
    public List<CourseDto> toDtoList(List<Courses> courses) {
        if ( courses == null ) {
            return null;
        }

        List<CourseDto> list = new ArrayList<CourseDto>( courses.size() );
        for ( Courses courses1 : courses ) {
            list.add( toDto( courses1 ) );
        }

        return list;
    }

    private String courseCategorieTitle(Courses courses) {
        if ( courses == null ) {
            return null;
        }
        Categorie categorie = courses.getCategorie();
        if ( categorie == null ) {
            return null;
        }
        String title = categorie.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}
