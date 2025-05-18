package com.example.freelanceFlow.Controllers;

import com.example.freelanceFlow.DTO.ProjectDTO;
import com.example.freelanceFlow.Models.Project;
import com.example.freelanceFlow.Models.User;
import com.example.freelanceFlow.Services.AuthService;
import com.example.freelanceFlow.Services.ProjectService;
import com.example.freelanceFlow.Services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
	private final AuthService authService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        try {
            Project savedProject = projectService.createProject(projectDTO);
            return ResponseEntity.ok(savedProject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    public ResponseEntity<List<Project>> getMyProjects(@RequestHeader("Authorization") String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            User currentUser = authService.getCurrentUser(cleanToken);
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Получаем проекты пользователя
            List<Project> projects = projectService.getProjectsByUser(currentUser);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        try {
            Project existingProject = projectService.getProjectById(id)
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            
            // Обновляем поля
            existingProject.setName(projectDTO.getName());
            existingProject.setDescription(projectDTO.getDescription());
            existingProject.setClientName(projectDTO.getClientName());
            existingProject.setBudget(projectDTO.getBudget());
            existingProject.setStatus(projectDTO.getStatus());
            existingProject.setStartDate(projectDTO.getStartDate());
            existingProject.setEndDate(projectDTO.getEndDate());
            
            Project updatedProject = projectService.saveProject(existingProject);
            
            // Конвертируем в DTO для ответа
            ProjectDTO responseDTO = convertToDTO(updatedProject);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setClientName(project.getClientName());
        dto.setBudget(project.getBudget());
        dto.setStatus(project.getStatus());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setUser_id(project.getUser().getId());
        return dto;
    }
}
