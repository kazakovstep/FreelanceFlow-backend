package com.example.freelanceFlow.Controllers;

import com.example.freelanceFlow.Models.Task;
import com.example.freelanceFlow.Models.Task.TaskStatus;
import com.example.freelanceFlow.Models.User;
import com.example.freelanceFlow.Services.ProjectService;
import com.example.freelanceFlow.Services.TaskService;
import com.example.freelanceFlow.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.saveTask(task));
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<Task>> getMyTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User currentUser = userService.getUserByEmail(email);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        List<Task> myTasks = taskService.getTasksByUser(currentUser);
        return ResponseEntity.ok(myTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}/status/{status}")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long taskId,
            @PathVariable String status
    ) {
        return taskService.getTaskById(taskId)
                .map(task -> {
                    task.setStatus(TaskStatus.valueOf(status));
                    Task updatedTask = taskService.saveTask(task);
                    return ResponseEntity.ok(updatedTask);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(
        @PathVariable Long taskId,
        @RequestBody Task taskData
    ) {
        Task updatedTask = taskService.updateTask(taskId, taskData);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}
