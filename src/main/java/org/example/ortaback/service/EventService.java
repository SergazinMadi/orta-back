package org.example.ortaback.service;

import org.example.ortaback.dto.request.EventCreateRequestDto;
import org.example.ortaback.dto.response.EventResponseDto;
import org.example.ortaback.dto.response.ParticipantResponseDto;
import org.example.ortaback.persistence.models.enums.EventType;

import java.util.List;

public interface EventService {
    
    EventResponseDto createEvent(EventCreateRequestDto requestDto, String username);
    
    EventResponseDto getEventById(Long eventId, String username);
    
    List<EventResponseDto> getAllActiveEvents(String username);
    
    List<EventResponseDto> getActiveEventsByType(EventType type, String username);
    
    EventResponseDto joinEvent(Long eventId, String username);
    
    EventResponseDto leaveEvent(Long eventId, String username);

    EventResponseDto cancelEvent(Long eventId, String username);

    EventResponseDto completeEvent(Long eventId, String username);

    EventResponseDto kickParticipant(Long eventId, Long participantId, String username);

    List<ParticipantResponseDto> getEventParticipants(Long eventId);

    List<EventResponseDto> getUserEvents(String username);
}
