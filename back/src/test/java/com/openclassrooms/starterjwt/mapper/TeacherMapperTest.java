package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class TeacherMapperTest {
    @Autowired
    TeacherMapper teacherMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void setup() {
        teacher = Teacher.builder()
                .lastName("lastname")
                .firstName("firstname")
                .build();
        teacherDto = new TeacherDto();
        teacherDto.setLastName("lastname");
        teacherDto.setFirstName("firstname");
    }

    @Test
    public void givenTeacher_whenCallToDto_thenReturnTeacherDto() {
        TeacherDto teacherDtoMapped = teacherMapper.toDto(teacher);

        assertEquals(teacher.getFirstName(), teacherDtoMapped.getFirstName());
    }

    @Test
    public void givenTeacherDto_whenCallToEntity_thenReturnTeacherEntity() {
        Teacher teacherMapped = teacherMapper.toEntity(teacherDto);

        assertEquals(teacherMapped.getFirstName(), teacherDto.getFirstName());
    }
}
