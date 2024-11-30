package com.example.freelanceFlow.Repositories;

import com.example.freelanceFlow.Models.Project;
import com.example.freelanceFlow.Models.Task;
import com.example.freelanceFlow.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    List<Task> findByUser(User user);
    List<Task> findByStatus(Task.TaskStatus status);
}
