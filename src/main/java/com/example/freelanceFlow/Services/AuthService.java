package com.example.freelanceFlow.Services;

import com.example.freelanceFlow.DTO.JwtRequest;
import com.example.freelanceFlow.DTO.JwtResponse;
import com.example.freelanceFlow.Models.User;
import com.example.freelanceFlow.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()
            ));
        } catch (BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public User getCurrentUser(String token) {
        try {
            token = token.trim();
            
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (token.isEmpty()) {
                return null;
            }
    
            String userEmail = jwtTokenUtils.getUsername(token);
            
            if (userEmail == null) {
                return null;
            }
    
            return userService.findByEmail(userEmail);
        } catch (Exception e) {
            return null;
        }
    }
}