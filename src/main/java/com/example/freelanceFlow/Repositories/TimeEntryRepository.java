package com.example.freelanceFlow.Repositories;

import com.example.freelanceFlow.Models.TimeEntry;
import com.example.freelanceFlow.Models.Task;
import com.example.freelanceFlow.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
    List<TimeEntry> findByTask(Task task);
    List<TimeEntry> findByTask_User_Id(Long userId);
}
