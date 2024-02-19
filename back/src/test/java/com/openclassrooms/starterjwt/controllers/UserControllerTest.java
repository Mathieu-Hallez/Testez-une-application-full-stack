package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Unit Test for User Controller")
@WithMockUser
public class UserControllerTest {

    private static final String USER_PATH = "/api/user";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user;
    private User user2;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .email("user")
                .lastName("lastname")
                .firstName("firstname")
                .password("1234")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        user2 = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("1234")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        when(userService.findById(1L)).thenReturn(user);
        when(userService.findById(2L)).thenReturn(null);
        when(userService.findById(3L)).thenReturn(user2);
    }

    @Test
    public void givenUserId_whenFindById_thenReturnResponseStatusOkWithUserFind() throws Exception {


        MvcResult result = mockMvc.perform(get(UserControllerTest.USER_PATH + "/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userMapper.toDto(user)));
        assertThat(result.getResponse().getContentAsString()).contains(user.getFirstName());
    }

    @Test
    public void givenUserId_whenCallFindById_thenReturnResponseWithStatusNotFound() throws Exception {
        mockMvc.perform(get(UserControllerTest.USER_PATH + "/{id}", "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenInvalidUserId_whenCallFindById_thenReturnResponseWithStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(get(UserControllerTest.USER_PATH + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void givenUserId_whenDelete_thenReturnResponseStatusOk() throws Exception {
        mockMvc.perform(delete(UserControllerTest.USER_PATH + "/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService, times(1)).delete(1L);
    }

    @Test
    public void givenUserId_whenDelete_thenReturnResponseStatusNotFound() throws Exception {
        mockMvc.perform(delete(UserControllerTest.USER_PATH + "/{id}", "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenInvalidUserId_whenDelete_thenReturnResponseStatusBadRequest() throws Exception {
        String id = "test";

        mockMvc.perform(delete(UserControllerTest.USER_PATH + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> Long.parseLong(id));
    }

    @Test
    public void givenUserId_whenDelete_thenReturnResponseStatusUnauthorized() throws Exception {
        mockMvc.perform(delete(UserControllerTest.USER_PATH + "/{id}", "3"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
