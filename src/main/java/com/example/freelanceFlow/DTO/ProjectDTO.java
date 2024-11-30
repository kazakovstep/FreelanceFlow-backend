package com.example.freelanceFlow.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.freelanceFlow.Models.Project.ProjectStatus;

import lombok.Data;

@Data
public class ProjectDTO {
		private Long id;
  	private String name;
    private String description;
    private String clientName;
    private BigDecimal budget;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long user_id;
}
