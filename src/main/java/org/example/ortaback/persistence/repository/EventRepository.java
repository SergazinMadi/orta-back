package org.example.ortaback.persistence.repository;

import org.example.ortaback.persistence.models.Event;
import org.example.ortaback.persistence.models.enums.EventStatus;
import org.example.ortaback.persistence.models.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findByStatusAndDateTimeAfter(EventStatus status, LocalDateTime dateTime);
    
    List<Event> findByStatusAndTypeAndDateTimeAfter(EventStatus status, EventType type, LocalDateTime dateTime);
    
    List<Event> findByHostId(Long hostId);
}
