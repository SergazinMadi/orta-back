package org.example.ortaback.persistence.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.ortaback.persistence.models.enums.EventStatus;
import org.example.ortaback.persistence.models.enums.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "place_name", length = 200)
    private String placeName;

    @Column(length = 500)
    private String address;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "time_duration")
    private Integer timeDuration; // в минутах

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "price_per_person", precision = 10, scale = 2)
    private BigDecimal pricePerPerson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = EventStatus.ACTIVE;
        }
    }
}
