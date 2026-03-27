package org.example.ortaback.persistence.repository;

import org.example.ortaback.persistence.models.Participant;
import org.example.ortaback.persistence.models.enums.RsvpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    
    List<Participant> findByEventIdAndRsvpStatus(Long eventId, RsvpStatus rsvpStatus);
    
    List<Participant> findByUserId(Long userId);
    
    Optional<Participant> findByEventIdAndUserId(Long eventId, Long userId);
    
    int countByEventIdAndRsvpStatus(Long eventId, RsvpStatus rsvpStatus);
    
    boolean existsByEventIdAndUserIdAndRsvpStatus(Long eventId, Long userId, RsvpStatus rsvpStatus);
}
