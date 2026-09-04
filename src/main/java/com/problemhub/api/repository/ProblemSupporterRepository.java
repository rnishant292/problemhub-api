package com.problemhub.api.repository;

import com.problemhub.api.model.ProblemSupporter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProblemSupporterRepository
        extends JpaRepository<ProblemSupporter, ProblemSupporter.ProblemSupporterId> {

    List<ProblemSupporter> findByProblemId(UUID problemId);

    boolean existsByProblemIdAndUserId(UUID problemId, UUID userId);
}
