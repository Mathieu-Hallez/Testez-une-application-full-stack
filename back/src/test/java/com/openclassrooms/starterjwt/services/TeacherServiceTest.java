package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test for Teacher Service.")
public class TeacherServiceTest {
    @Mock
    TeacherRepository teacherRepository;

    @InjectMocks
    TeacherService teacherService;

    private List<Teacher> teachers;
    private Teacher teacher;

    @BeforeEach
    public void setup() {
        teacher = Teacher.builder()
                .id(1L)
                .lastName("lastname")
                .firstName("firstname")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        teachers = new ArrayList<>();
        teachers.add(teacher);
    }

    @Test
    public void whenFindAll_thenReturnAListOfAllTeachers() {
        when(teacherRepository.findAll()).thenReturn(teachers);
        List<Teacher> teachersFound = teacherService.findAll();

        assertThat(teachersFound).isInstanceOf(List.class);
        assertThat(teachersFound.size()).isEqualTo(1);
    }

    @Test
    public void givenTeacherId_whenFindTeacherByID_thenReturnNullWhenNotFind() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(teacherService.findById(1L)).isNull();
    }

    @Test
    public void givenTeacherId_whenFindTeacherByID_thenReturnTheJavaTeacherObject() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.ofNullable(teacher));

        assertThat(teacherService.findById(1L).getFirstName()).isEqualTo("firstname");
    }
}
