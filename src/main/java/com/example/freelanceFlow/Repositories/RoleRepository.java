package com.example.freelanceFlow.Repositories;

import com.example.freelanceFlow.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}