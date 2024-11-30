package com.example.freelanceFlow.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TimeEntryDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private boolean isBillable;
		private Long user_id;
}
