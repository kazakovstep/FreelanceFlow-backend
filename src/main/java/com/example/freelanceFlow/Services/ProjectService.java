package com.example.freelanceFlow.Services;

import com.example.freelanceFlow.DTO.ProjectDTO;
import com.example.freelanceFlow.Models.Project;
import com.example.freelanceFlow.Models.User;
import com.example.freelanceFlow.Repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Project createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setClientName(projectDTO.getClientName());
        project.setBudget(projectDTO.getBudget());
        project.setStatus(projectDTO.getStatus());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        
        User user = userService.getUserById(projectDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + projectDTO.getUser_id()));
        project.setUser(user);
        
        return projectRepository.save(project);
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getProjectsByUser(User user) {
        return projectRepository.findByUser(user);
    }

    public List<Project> getProjectsByStatus(Project.ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
