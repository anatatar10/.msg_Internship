package com.calypso.binar.security;

import com.calypso.binar.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);  // Set the mocked security context
    }

    @Test
    void doFilterInternal_validJwt_shouldAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        when(jwtService.validateJwtToken("validJwtToken")).thenReturn(true);
        when(jwtService.getUserNameFromJwtToken("validJwtToken")).thenReturn("testUser");
        when(userService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).validateJwtToken("validJwtToken");
        verify(jwtService, times(1)).getUserNameFromJwtToken("validJwtToken");
        verify(userService, times(1)).loadUserByUsername("testUser");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(securityContext).setAuthentication(authenticationCaptor.capture());

        UsernamePasswordAuthenticationToken authentication = authenticationCaptor.getValue();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void foFilterInternal_invalidJwt_shouldNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidJwtToken");
        when(jwtService.validateJwtToken("invalidJwtToken")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).validateJwtToken("invalidJwtToken");
        verify(jwtService, never()).getUserNameFromJwtToken(anyString());
        verify(userService, never()).loadUserByUsername(anyString());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_noJwt_shouldNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, never()).validateJwtToken(anyString());
        verify(jwtService, never()).getUserNameFromJwtToken(anyString());
        verify(userService, never()).loadUserByUsername(anyString());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_exceptionThrown_shouldNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        when(jwtService.validateJwtToken("validJwtToken")).thenThrow(new RuntimeException("Exception occurred"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).validateJwtToken("validJwtToken");
        verify(jwtService, never()).getUserNameFromJwtToken(anyString());
        verify(userService, never()).loadUserByUsername(anyString());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
