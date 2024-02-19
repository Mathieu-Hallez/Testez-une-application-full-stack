package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionMapperTest {

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    @Autowired
    private SessionMapper sessionMapper;

    private SessionDto sessionDto;
    private Session session;
    private User user;

    @BeforeEach
    public void setup() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-02-12");
        sessionDto = new SessionDto(
                1L,
                "sessionName",
                date,
                1L,
                "description",
                List.of(1L),
                null,
                null
        );
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("lastName")
                .firstName("firstname")
                .build();
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("firstname")
                .lastName("lastname")
                .password("password")
                .build();
        session = Session.builder()
                .id(1L)
                .name("sessionName")
                .date(date)
                .teacher(teacher)
                .users(List.of(user))
                .description("description")
                .build();


        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);
    }

    @Test
    public void givenSessionDto_whenMapToEntity_thenReturnSession() {
        // Mocks

        Session sessionMapped = sessionMapper.toEntity(sessionDto);

        assertEquals(session.getId(), sessionMapped.getId());
        assertThat(session.getUsers().get(0).getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void givenSessiontDtoWithUnknownUsers_whenMapToEntity_thenReturnSessionWithNoUsers() {
        session.setUsers(List.of());

        Session sessionMapped = sessionMapper.toEntity(sessionDto);

        assertEquals(session.getId(), sessionMapped.getId());
        assertThat(session.getUsers()).isEmpty();
    }

    @Test
    public void givenSessiontDtoWithNoTeacher_whenMapToEntity_thenReturnSessionWithNoTeacher() {
        session.setTeacher(null);

        Session sessionMapped = sessionMapper.toEntity(sessionDto);

        assertEquals(session.getId(), sessionMapped.getId());
        assertThat(session.getTeacher()).isNull();
    }

    @Test
    public void givenSessionDtoList_whenMapToEntities_thenReturnSessionEntityList() {
        List<SessionDto> sessionDtos = new ArrayList<>();
        sessionDtos.add(sessionDto);

        List<Session> sessionListMapped = sessionMapper.toEntity(sessionDtos);

        assertThat(sessionListMapped).hasSize(1);
        assertEquals(sessionDto.getName(),sessionListMapped.get(0).getName());
    }
}
