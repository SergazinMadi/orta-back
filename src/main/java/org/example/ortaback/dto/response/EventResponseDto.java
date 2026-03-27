package org.example.ortaback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ortaback.persistence.models.enums.EventStatus;
import org.example.ortaback.persistence.models.enums.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDto {

    private Long id;
    private Long hostId;
    private String hostName;
    private String hostFullName;
    private EventStatus status;
    private String title;
    private EventType type;
    private String description;
    private String placeName;
    private String address;
    private LocalDateTime dateTime;
    private Integer timeDuration;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private BigDecimal pricePerPerson;
    private LocalDateTime createdAt;
    private Boolean isUserJoined;
}
