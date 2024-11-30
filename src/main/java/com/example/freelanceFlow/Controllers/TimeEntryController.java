package com.example.freelanceFlow.Controllers;

import com.example.freelanceFlow.DTO.TimeEntryDTO;
import com.example.freelanceFlow.Models.TimeEntry;
import com.example.freelanceFlow.Services.TimeEntryService;
import com.example.freelanceFlow.Services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TimeEntryController {
    private final TimeEntryService timeEntryService;

    // Эндпоинты для конкретных задач
    @PostMapping("/api/tasks/{taskId}/time")
    public ResponseEntity<TimeEntry> createTimeEntry(
            @PathVariable Long taskId,
            @RequestBody TimeEntryDTO timeEntryDTO) {
        TimeEntry createdEntry = timeEntryService.createTimeEntry(timeEntryDTO, taskId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @GetMapping("/api/tasks/{taskId}/time")
    public ResponseEntity<List<TimeEntry>> getTimeEntries(
            @PathVariable Long taskId) {
        return ResponseEntity.ok(timeEntryService.getTimeEntriesByTask(taskId));
    }

    @DeleteMapping("/api/tasks/{taskId}/time/{entryId}")
    public ResponseEntity<Void> deleteTimeEntry(
            @PathVariable Long taskId,
            @PathVariable Long entryId) {
        timeEntryService.deleteTimeEntry(entryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/users/{userId}/time-statistics")
    public ResponseEntity<Map<String, Object>> getUserTimeStatistics(
            @PathVariable Long userId) {
        Map<String, Object> statistics = timeEntryService.getUserTimeStatistics(userId);
        return ResponseEntity.ok(statistics);
    }
}