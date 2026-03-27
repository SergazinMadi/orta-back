package org.example.ortaback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponseDto {

    private Long id;
    private Long eventId;
    private String eventTitle;
    private Long hostId;
    private String hostName;
    private String hostFullName;
    private Long reviewerId;
    private String reviewerName;
    private String reviewerFullName;
    private Integer score;
    private String comment;
    private LocalDateTime createdAt;
}
