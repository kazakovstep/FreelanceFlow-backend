package com.example.freelanceFlow.DTO;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
