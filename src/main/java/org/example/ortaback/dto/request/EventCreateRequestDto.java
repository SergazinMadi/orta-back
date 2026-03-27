package org.example.ortaback.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ortaback.persistence.models.enums.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotNull(message = "Event type is required")
    private EventType type;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Size(max = 200, message = "Place name must not exceed 200 characters")
    private String placeName;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @NotNull(message = "Date and time is required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime dateTime;

    @Positive(message = "Duration must be positive")
    private Integer timeDuration; // в минутах

    @Positive(message = "Max participants must be positive")
    private Integer maxParticipants;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private BigDecimal pricePerPerson;
}
