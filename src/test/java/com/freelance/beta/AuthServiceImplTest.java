package com.freelance.beta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.example.freelanceFlow.Configurations.UserDetail;
import com.example.freelanceFlow.DTO.JwtRequest;
import com.example.freelanceFlow.DTO.JwtResponse;
import com.example.freelanceFlow.Models.User;
import com.example.freelanceFlow.Services.AuthService;
import com.example.freelanceFlow.Services.UserService;
import com.example.freelanceFlow.utils.JwtTokenUtils;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationTokenCaptor;

    @Test
    public void testCreateAuthToken_WithValidCredentials_ReturnsToken() {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        UserDetail userDetails = new UserDetail(user);

        String token = "testToken";

        when(userService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn(token);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword(), new ArrayList<>()));

        ResponseEntity<?> response = authService.createAuthToken(authRequest);

        verify(authenticationManager).authenticate(authenticationTokenCaptor.capture());
        UsernamePasswordAuthenticationToken authenticationToken = authenticationTokenCaptor.getValue();

        assertEquals(authRequest.getEmail(), authenticationToken.getPrincipal());
        assertEquals(authRequest.getPassword(), authenticationToken.getCredentials());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new JwtResponse(token), response.getBody());
    }
}