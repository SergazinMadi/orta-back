package org.example.ortaback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ortaback.dto.request.RatingCreateRequestDto;
import org.example.ortaback.dto.response.RatingResponseDto;
import org.example.ortaback.persistence.models.User;
import org.example.ortaback.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingResponseDto> rateHost(
            @Valid @RequestBody RatingCreateRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        RatingResponseDto response = ratingService.rateHost(requestDto, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<RatingResponseDto>> getHostRatings(@PathVariable Long hostId) {
        List<RatingResponseDto> response = ratingService.getHostRatings(hostId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/host/{hostId}/average")
    public ResponseEntity<Double> getHostAverageRating(@PathVariable Long hostId) {
        Double average = ratingService.getHostAverageRating(hostId);
        return ResponseEntity.ok(average);
    }
}
