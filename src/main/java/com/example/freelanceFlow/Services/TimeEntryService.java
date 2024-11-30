package com.example.freelanceFlow.Services;

import com.example.freelanceFlow.Models.TimeEntry;
import com.example.freelanceFlow.DTO.TimeEntryDTO;
import com.example.freelanceFlow.Models.Task;
import com.example.freelanceFlow.Models.User;
import com.example.freelanceFlow.Repositories.TimeEntryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeEntryService {
    private final TimeEntryRepository timeEntryRepository;
    private final TaskService taskService;
    private final UserService userService;

    @Transactional
    public TimeEntry createTimeEntry(TimeEntryDTO dto, Long taskId) {
        Task task = taskService.getTaskById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        User currentUser = userService.getUserById(dto.getUser_id())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + taskId));;

        validateTimeEntry(dto);

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setTask(task);
        timeEntry.setUser(currentUser); // Устанавливаем текущего пользователя
        timeEntry.setStartTime(dto.getStartTime());
        timeEntry.setEndTime(dto.getEndTime());
        timeEntry.setDescription(dto.getDescription());
        timeEntry.setBillable(dto.isBillable());
        timeEntry.setDuration(calculateDuration(dto.getStartTime(), dto.getEndTime()));

        return timeEntryRepository.save(timeEntry);
    }

    private void validateTimeEntry(TimeEntryDTO dto) {
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time must be provided");
        }

        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
    }

    private Integer calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return (int) ChronoUnit.MINUTES.between(startTime, endTime);
    }


    public List<TimeEntry> getTimeEntriesByTask(Long taskId) {
        Task task = taskService.getTaskById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return timeEntryRepository.findByTask(task);
    }

    public void deleteTimeEntry(Long id) {
        timeEntryRepository.deleteById(id);
    }

    public Map<String, Object> getUserTimeStatistics(Long userId) {
        List<TimeEntry> userTimeEntries = timeEntryRepository.findByTask_User_Id(userId);

        System.out.println(userTimeEntries);
        
        int totalMinutes = userTimeEntries.stream()
                .mapToInt(TimeEntry::getDuration)
                .sum();
        
        int billableMinutes = userTimeEntries.stream()
                .filter(TimeEntry::isBillable)
                .mapToInt(TimeEntry::getDuration)
                .sum();
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalHours", totalMinutes / 60.0);
        statistics.put("billableHours", billableMinutes / 60.0);
        statistics.put("totalMinutes", totalMinutes);
        statistics.put("billableMinutes", billableMinutes);
        statistics.put("timeEntries", userTimeEntries);
        
        return statistics;
    }
}