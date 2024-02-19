package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthTokenFilterIT {
    @Autowired
    private AuthTokenFilter authTokenFilter;

    @MockBean
    private HttpServletRequest httpServletRequest;
    @MockBean
    private HttpServletResponse httpServletResponse;
    @MockBean
    private FilterChain filterChain;
    @MockBean
    private HttpSession httpSession;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtUtils jwtUtils;

    private User user;

    @BeforeEach
    public void setup() {
        String authorisationString = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("firstname")
                .lastName("lastname")
                .password("123456")
                .build();

        when(httpServletRequest.getHeader("Authorization")).thenReturn(authorisationString);
        when(httpSession.getId()).thenReturn("Session1");
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getRemoteAddr()).thenReturn("remoteAddr1");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("test@test.com");
    }

    @Test
    @Disabled
    public void givenHttpServletRequestAndHttpServletResponseAndFilterChain_whenDoFilterInternal_thenSecurityContextHolderHasAuthenticationAndExecuteDoFilter() throws ServletException, IOException {

        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);

        authTokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo(user.getEmail());
        verify(filterChain, times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }
}
