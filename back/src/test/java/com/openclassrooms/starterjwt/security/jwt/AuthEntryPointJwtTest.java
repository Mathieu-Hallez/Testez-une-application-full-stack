package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.internal.ByteArrays;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthEntryPointJwtTest {

    @MockBean
    Logger logger;

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Autowired
    private HttpServletResponse httpServletResponse;

    @Test
    public void testCommenceMethod() throws ServletException, IOException {
        when(httpServletRequest.getServletPath()).thenReturn("servlet/path");
        AuthenticationException authenticationException = new AuthenticationException("ExceptionMessage") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };

        authEntryPointJwt.commence(httpServletRequest,httpServletResponse,authenticationException);

        assertEquals(httpServletResponse.getStatus(), HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(httpServletResponse.getContentType(), MediaType.APPLICATION_JSON_VALUE);

    }
}
