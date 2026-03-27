package org.example.ortaback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ortaback.persistence.models.enums.RsvpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponseDto {

    private Long id;
    private Long userId;
    private String userName;
    private String userFullName;
    private Boolean isHoster;
    private RsvpStatus rsvpStatus;
    private Boolean attendAt;
    private LocalDateTime joinedAt;
}
