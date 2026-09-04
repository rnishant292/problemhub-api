package com.problemhub.api.dto;

import lombok.Data;

@Data
public class ProblemRequest {
    private String title;
    private String description;
    private String category;
}
