package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterIT {
    @InjectMocks
    AuthTokenFilter authTokenFilter;


    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    HttpServletResponse httpServletResponse;
    @Mock
    FilterChain filterChain;
    @Mock
    UserDetailsServiceImpl userDetailsService;
    @Mock
    JwtUtils jwtUtils;

    @Test
    @Disabled
    public void givenHttpServletRequestAndHttpServletResponseAndFilterChain_whenDoFilterInternal_thenSecurityContextHolderHasAuthenticationAndExecuteDoFilter() throws ServletException, IOException {
        String authorisationString = "Bearer monJwtToken";

        when(httpServletRequest.getHeader("Authorization")).thenReturn(authorisationString);

        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);

        authTokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
    }
}
