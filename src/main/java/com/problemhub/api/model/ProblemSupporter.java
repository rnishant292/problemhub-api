package com.problemhub.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "problem_supporters")
@Data
@IdClass(ProblemSupporter.ProblemSupporterId.class)
public class ProblemSupporter {

    @Id
    @Column(name = "problem_id")
    private UUID problemId;

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Data
    public static class ProblemSupporterId implements Serializable {
        private UUID problemId;
        private UUID userId;
    }
}
