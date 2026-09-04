package com.problemhub.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProblemResponse {
    private UUID id;
    private String title;
    private String description;
    private String category;
    private String language;
    private UUID createdBy;
    private Instant createdAt;
    private long supportCount;
}
