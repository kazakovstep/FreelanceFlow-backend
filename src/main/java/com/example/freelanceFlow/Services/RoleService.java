package com.example.freelanceFlow.Services;

import com.example.freelanceFlow.Models.Role;
import com.example.freelanceFlow.Repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER");
    }

    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN");
    }
}