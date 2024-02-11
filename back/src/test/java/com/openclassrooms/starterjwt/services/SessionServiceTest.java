package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test for Session Service")
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
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
    }

    // Create a session
    @Test
    public void givenASessionObject_whenCreateSession_thenReturnTheSessionCreate() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        assertEquals(session.getName(), sessionService.create(session).getName());
    }

    // Delete a session
    @Test
    public void givenASessionId_whenDeleteASession_thenCallDeleteByIdMethodOfSessionRepository() {
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    // findAll sessions
    @Test
    public void shouldGetAListOfAllSessionAvailable() {
        List<Session> sessions = new ArrayList<Session>();
        sessions.add(session);

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> sessionsTested = sessionService.findAll();
        assertThat(sessionsTested).isInstanceOf(List.class);
        assertThat(sessionsTested.size()).isEqualTo(1);
        assertThat(sessionsTested.get(0)).isInstanceOf(Session.class);
    }

    // getBytId a session
    @Test
    public void givenSessionId_whenGetSessionById_thenReturnSessionWithSameId() {

        when(sessionRepository.findById(1L)).thenReturn(Optional.ofNullable(session));

        Session sessionExpected = sessionService.getById(1L);
        assertThat(sessionExpected.getId()).isEqualTo(session.getId());
    }
    @Test
    public void givenUnknownSessionId_whenGetSessionById_thenReturnNUll() {

        assertThat(sessionService.getById(2L)).isNull();
    }

    // Update a session
    @Test
    public void givenSessionIdAndEditedSession_whenUpdateASession_thenReturnSessionUpdated() {
        Session session1 = Session.builder()
                .name("session1")
                .build();

        when(sessionRepository.save(any(Session.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Session sessionUpdated = sessionService.update(1L, session1);
        assertThat(sessionUpdated.getId()).isEqualTo(1L);
        assertThat(sessionUpdated.getName()).isEqualTo("session1");
    }
    // Participate to a session

    // - session not found
    @Test
    public void givenSessionIdAndUserId_whenParticipateToASession_thenThrowNotFoundExceptionForTheSession() {

        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> sessionService.participate(2L, 1L)
        );
    }

    // - user not found
    @Test
    public void givenSessionIdAndUserId_whenParticipateToASession_thenThrowNotFoundExceptionForTheUser() {

        when(sessionRepository.findById(anyLong())).thenReturn(Optional.ofNullable(session));

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> sessionService.participate(1L, 2L)
        );
    }
    // - user already participate in the session
    @Test
    public void givenSessionIdAndUserId_whenParticipateToASession_thenThrowBadRequestExceptionBecauseAlreadyParticipate() {
        // Given
        List<User> participants = new ArrayList<>();
        participants.add(user);
        session.setUsers(participants);

        // When
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.ofNullable(session));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(
                () -> sessionService.participate(1L, 1L)
        );
    }
    // - successfully participate
    @Test
    public void givenSessionIdAndUserId_whenParticipateToASession_thenSuccessfullyAddUserAndSaveIt() {
        // When
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        sessionService.participate(1L,1L);

        // Then
        verify(sessionRepository, times(1)).save(any(Session.class));
        assertThat(session.getUsers()).contains(user);
    }

    // No longer participate to a session
    // - session not found
    @Test
    public void givenSessionIdAndUserId_whenNoLongerParticipateToASession_thenThrowNotFoundExceptionForTheSession() {
        // When
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> sessionService.noLongerParticipate(1L, 1L));
    }
    // - user no already participate to the session
    @Test
    @DisplayName("Unsubscribe a user of a session but the user not already participate to the session.")
    public void givenSessionIdAndUserId_whenNoLongerParticipateToASession_thenThrowBadRequestException() {

        when(sessionRepository.findById(anyLong())).thenReturn(Optional.ofNullable(session));

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> sessionService.noLongerParticipate(1L, 1L));
    }
    // - successfully leave session
    @Test
    public void givenSessionIdAndUserId_whenNoLongerParticipateToASession_thenSuccessfullyNoLongerParticipate() {

        // Given
        session.getUsers().add(user);

        // When
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.ofNullable(session));
        sessionService.noLongerParticipate(1L,1L);

        // Then
        verify(sessionRepository, times(1)).save(any(Session.class));
        assertThat(session.getUsers()).doesNotContain(user);
    }
}