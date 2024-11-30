package com.example.freelanceFlow.Services;

import com.example.freelanceFlow.Models.Task;
import com.example.freelanceFlow.Models.Project;
import com.example.freelanceFlow.Models.User;
import com.example.freelanceFlow.Repositories.TaskRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByProject(Project project) {
        return taskRepository.findByProject(project);
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task updateTask(Long taskId, Task taskData) {
        Task existingTask = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        // Сохраняем существующего пользователя
        User existingUser = existingTask.getUser();
        
        // Обновляем поля
        existingTask.setTitle(taskData.getTitle());
        existingTask.setDescription(taskData.getDescription());
        existingTask.setStatus(taskData.getStatus());
        existingTask.setPriority(taskData.getPriority());
        existingTask.setDueDate(taskData.getDueDate());
        
        // Восстанавливаем связь с пользователем
        existingTask.setUser(existingUser);
        
        return taskRepository.save(existingTask);
    }
}
