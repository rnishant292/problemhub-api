package com.problemhub.api.controller;

import com.problemhub.api.dto.ProblemRequest;
import com.problemhub.api.dto.ProblemResponse;
import com.problemhub.api.model.Problem;
import com.problemhub.api.model.ProblemSupporter;
import com.problemhub.api.repository.ProblemRepository;
import com.problemhub.api.repository.ProblemSupporterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemRepository problemRepository;
    private final ProblemSupporterRepository supporterRepository;

    public ProblemController(ProblemRepository problemRepository,
                              ProblemSupporterRepository supporterRepository) {
        this.problemRepository = problemRepository;
        this.supporterRepository = supporterRepository;
    }

    @GetMapping
    public List<ProblemResponse> list() {
        return problemRepository.findAll().stream()
                .map(this::toResponse)
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> get(@PathVariable UUID id) {
        return problemRepository.findById(id)
                .map(p -> ResponseEntity.ok(toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProblemResponse> create(@RequestBody ProblemRequest request, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UUID userId = UUID.fromString(jwt.getSubject());

        Problem problem = new Problem();
        problem.setTitle(request.getTitle());
        problem.setDescription(request.getDescription());
        problem.setCategory(request.getCategory());
        problem.setCreatedBy(userId);
        problem.setCreatedAt(Instant.now());

        Problem saved = problemRepository.save(problem);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PostMapping("/{id}/support")
    public ResponseEntity<Void> support(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UUID userId = UUID.fromString(jwt.getSubject());

        if (supporterRepository.existsByProblemIdAndUserId(id, userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        ProblemSupporter supporter = new ProblemSupporter();
        supporter.setProblemId(id);
        supporter.setUserId(userId);
        supporter.setCreatedAt(Instant.now());
        supporterRepository.save(supporter);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // One query per problem to count supporters — fine at this scale.
    // If the list grows large, this is the first place to optimize.
    private ProblemResponse toResponse(Problem p) {
        long count = supporterRepository.findByProblemId(p.getId()).size();
        return new ProblemResponse(
                p.getId(), p.getTitle(), p.getDescription(), p.getCategory(),
                p.getLanguage(), p.getCreatedBy(), p.getCreatedAt(), count
        );
    }
}
