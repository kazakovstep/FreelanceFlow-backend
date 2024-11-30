package com.example.freelanceFlow.Repositories;

import com.example.freelanceFlow.Models.Project;
import com.example.freelanceFlow.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
    List<Project> findByStatus(Project.ProjectStatus status);
}
