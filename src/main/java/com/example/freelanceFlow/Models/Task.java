package com.example.freelanceFlow.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Entity
@Table(name = "tasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Project project;

    @JsonProperty("project_id")
    public Long getProjectId() {
        return project != null ? project.getId() : null;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    @JsonProperty("user_id")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private BigDecimal estimatedHours;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TimeEntry> timeEntries;

    @Transient
    public Integer getTotalTimeSpent() {
        if (timeEntries == null || timeEntries.isEmpty()) {
            return 0;
        }
        return timeEntries.stream()
                .map(TimeEntry::getDuration)
                .filter(duration -> duration != null)
                .reduce(0, Integer::sum);
    }

    @Transient
    public Double getTotalTimeSpentHours() {
        return getTotalTimeSpent() / 60.0;
    }

    @Transient
    public Integer getTotalBillableTime() {
        if (timeEntries == null || timeEntries.isEmpty()) {
            return 0;
        }
        return timeEntries.stream()
                .filter(TimeEntry::isBillable)
                .map(TimeEntry::getDuration)
                .filter(duration -> duration != null)
                .reduce(0, Integer::sum);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum TaskStatus {
        TO_DO, IN_PROGRESS, DONE, DELETED
    }

    public enum TaskPriority {
        LOW, MEDIUM, HIGH
    }
}
