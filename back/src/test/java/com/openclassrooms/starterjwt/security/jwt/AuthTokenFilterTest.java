package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {
    @InjectMocks
    AuthTokenFilter authTokenFilter;

    @Mock
    HttpServletRequest httpServletRequest;

    @Test
    public void givenHttpServletRequest_whenParseJwt_thenReturnJwtToken() {
        String authorisationString = "Bearer monJwtToken";


    }
}
