package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Unit Test for Teacher Controller")
@WithMockUser
public class TeacherControllerTest {

    private static final String TEACHER_PATH = "/api/teacher";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeacherService teacherService;

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
    }

    @Test
    public void givenTeacherId_whenCallFindById_thenReturnResponseWithStatusOKAndTheTeacherExpected() throws Exception {
        when(teacherService.findById(1L)).thenReturn(teacher);

        MvcResult result = mockMvc.perform(get(TeacherControllerTest.TEACHER_PATH + "/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(teacherMapper.toDto(teacher)));
        assertThat(result.getResponse().getContentAsString()).contains(teacher.getFirstName());
    }

    @Test
    public void givenTeacherId_whenCallFindById_thenReturnResponseWithStatusNotFound() throws Exception {
        when(teacherService.findById(2L)).thenReturn(null);

        mockMvc.perform(get(TeacherControllerTest.TEACHER_PATH + "/{id}", "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenInvalidTeacherId_whenCallFindById_thenReturnResponseWithStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(get(TeacherControllerTest.TEACHER_PATH + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void whenCallFindAll_thenResponseOkStatusWithAListOfTeachers() throws Exception {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        List<TeacherDto> dtoTeachers = this.teacherMapper.toDto(teachers);
        when(teacherService.findAll()).thenReturn(teachers);


        MvcResult result = mockMvc.perform(get(TeacherControllerTest.TEACHER_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<TeacherDto> teachersDtoReceived = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), TeacherDto[].class));

        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(dtoTeachers));
        assertThat(teachersDtoReceived.size()).isEqualTo(1);
        assertThat(teachersDtoReceived.get(0).getFirstName()).isEqualTo(teacher.getFirstName());
    }
}
