package org.example.ortaback.persistence.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.ortaback.persistence.models.enums.RsvpStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "participants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_hoster", nullable = false)
    private Boolean isHoster;

    @Enumerated(EnumType.STRING)
    @Column(name = "rsvp_status", nullable = false, length = 20)
    private RsvpStatus rsvpStatus;

    @Column(name = "attend_at")
    private Boolean attendAt;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        this.joinedAt = LocalDateTime.now();
        if (this.isHoster == null) {
            this.isHoster = false;
        }
        if (this.rsvpStatus == null) {
            this.rsvpStatus = RsvpStatus.JOINED;
        }
    }
}
