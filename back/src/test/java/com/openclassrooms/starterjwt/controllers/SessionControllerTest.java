package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testing session controller.")
@WithMockUser
public class SessionControllerTest {

    private static final String SESSION_PATH = "/api/session";

    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    private Session session;
    private SessionDto sessionDto;
    private User user;
    private Teacher teacher;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("1234")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        teacher = Teacher.builder()
                .id(1L)
                .lastName("lastname")
                .firstName("firstname")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        session = Session.builder()
                .name("session1")
                .id(1L)
                .date(Date.from(Instant.now()))
                .description("description")
                .teacher(teacher)
                .users(new ArrayList<User>())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        sessionDto = sessionMapper.toDto(session);

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionService.getById(2L)).thenReturn(null);
    }

    @Test
    public void givenAnExistIdSession_whenCallFindById_thenResponseOKStatusWithSessionDto() throws Exception {
        mockMvc.perform(get(SessionControllerTest.SESSION_PATH + "/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(sessionMapper.toDto(session))));


    }

    @Test
    public void givenAnIdSession_whenCallFindById_thenResponseNotFound() throws Exception {
        mockMvc.perform(get(SessionControllerTest.SESSION_PATH + "/{id}", "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenAString_whenCallFindById_thenResponseBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(get(SessionControllerTest.SESSION_PATH + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void whenCallFindAll_thenResponseOkStatusWithAListOfSessions() throws Exception {
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        List<SessionDto> dtoSessions = this.sessionMapper.toDto(sessions);
        when(sessionService.findAll()).thenReturn(sessions);


        MvcResult result = mockMvc.perform(get(SessionControllerTest.SESSION_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<SessionDto> sessionsDtoReceived = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), SessionDto[].class));

        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(dtoSessions));
        assertThat(sessionsDtoReceived.size()).isEqualTo(1);
        assertThat(sessionsDtoReceived.get(0).getName()).isEqualTo(session.getName());
    }

    @Test
    public void givenValidSessionDto_whenCallCreate_thenResponseOkStatusWithTheNewSessionDtoCreated() throws Exception {

        when(sessionService.create(any(Session.class))).thenReturn(session);

        MvcResult result = mockMvc.perform(
                    post(SessionControllerTest.SESSION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(),SessionDto.class).getName()).isEqualTo(sessionDto.getName());
    }

    @Test
    public void givenInvalidSessionDto_whenCallCreate_thenResponseBadRequest() throws Exception {

        String incorrectSessionDtoJsonString = objectMapper.writeValueAsString(sessionDto).replaceAll("\"name\":\"session1\"", "");

        mockMvc.perform(
                post(SessionControllerTest.SESSION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incorrectSessionDtoJsonString)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenSessionIdToUpdateAndSessionDtoForUpdate_whenCallUpdate_thenReturnResponseStatusOkWithSessionDtoUpdated() throws Exception {
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session);

        MvcResult result = mockMvc.perform(
                put(SessionControllerTest.SESSION_PATH + "/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto))
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(session.getName());
    }

    @Test
    public void givenInvalidSessionIdAndSessionDtoForUpdate_whenCallUpdate_thenReturnResponseStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(
                put(SessionControllerTest.SESSION_PATH + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void givenSessionIdAndInvalidSessionDtoForUpdate_whenCallUpdate_thenReturnResponseStatusBadRequest() throws Exception {
        String incorrectSessionDtoJsonString = objectMapper.writeValueAsString(sessionDto).replaceAll("\"name\":\"session1\"", "");

        mockMvc.perform(
                put(SessionControllerTest.SESSION_PATH + "/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incorrectSessionDtoJsonString)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenSessionId_whenDeleteASession_thenReturnResponseStatusOK() throws Exception {
        mockMvc.perform(delete(SessionControllerTest.SESSION_PATH + "/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(sessionService,times(1)).delete(1L);
    }

    @Test
    public void givenSessionId_whenDeleteASession_thenReturnResponseStatusNotFound() throws Exception {
        mockMvc.perform(delete(SessionControllerTest.SESSION_PATH + "/{id}", "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenInvalidSessionId_whenDeleteASession_thenReturnResponseStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(delete(SessionControllerTest.SESSION_PATH + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void givenSessionIdAndUserId_whenParticipate_thenReturnResponseStatusOk() throws Exception {
        mockMvc.perform(post(SessionControllerTest.SESSION_PATH + "/{id}/participate/{userId}", "1", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(sessionService, times(1)).participate(1L,1L);
    }

    @Test
    public void givenInvalidSessionIdAndUserId_whenParticipate_thenReturnResponseStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(post(SessionControllerTest.SESSION_PATH + "/{id}/participate/{userId}", id, "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void givenSessionIdAndInvalidUserId_whenParticipate_thenReturnResponseStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(post(SessionControllerTest.SESSION_PATH + "/{id}/participate/{userId}", "1", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void givenSessionIdAndUserId_whenNoLongerParticipate_thenReturnResponseStatusOk() throws Exception {
        mockMvc.perform(delete(SessionControllerTest.SESSION_PATH + "/{id}/participate/{userId}", "1", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(sessionService, times(1)).noLongerParticipate(1L,1L);
    }

    @Test
    public void givenInvalidSessionIdAndUserId_whenNoLongerParticipate_thenReturnResponseStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(delete(SessionControllerTest.SESSION_PATH + "/{id}/participate/{userId}", id, "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void givenSessionIdAndInvalidUserId_whenNoLongerParticipate_thenReturnResponseStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(delete(SessionControllerTest.SESSION_PATH + "/{id}/participate/{userId}", "1", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }
    // NoLongerPartcipate
    // - OK
    // - BadRequest NumberFormatException

}
