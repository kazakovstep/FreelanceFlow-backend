package com.example.freelanceFlow.DTO;


import com.example.freelanceFlow.Models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private List<Role> role;
}
