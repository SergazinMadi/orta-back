package org.example.ortaback.service;

import org.example.ortaback.dto.request.RatingCreateRequestDto;
import org.example.ortaback.dto.response.RatingResponseDto;

import java.util.List;

public interface RatingService {
    
    RatingResponseDto rateHost(RatingCreateRequestDto requestDto, String username);
    
    List<RatingResponseDto> getHostRatings(Long hostId);
    
    Double getHostAverageRating(Long hostId);
}
