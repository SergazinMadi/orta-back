package org.example.ortaback.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ortaback.dto.request.EventCreateRequestDto;
import org.example.ortaback.dto.response.EventResponseDto;
import org.example.ortaback.dto.response.ParticipantResponseDto;
import org.example.ortaback.exception.ResourceAlreadyExistsException;
import org.example.ortaback.exception.ResourceNotFoundException;
import org.example.ortaback.mapper.EventMapper;
import org.example.ortaback.mapper.ParticipantMapper;
import org.example.ortaback.persistence.models.Event;
import org.example.ortaback.persistence.models.Participant;
import org.example.ortaback.persistence.models.User;
import org.example.ortaback.persistence.models.enums.EventStatus;
import org.example.ortaback.persistence.models.enums.EventType;
import org.example.ortaback.persistence.models.enums.RsvpStatus;
import org.example.ortaback.persistence.repository.EventRepository;
import org.example.ortaback.persistence.repository.ParticipantRepository;
import org.example.ortaback.persistence.repository.UserRepository;
import org.example.ortaback.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final ParticipantMapper participantMapper;

    @Override
    @Transactional
    public EventResponseDto createEvent(EventCreateRequestDto requestDto, String username) {
        User host = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Event event = eventMapper.toEntity(requestDto);
        event.setHost(host);

        Event savedEvent = eventRepository.save(event);

        // Автоматически добавляем хоста как участника
        Participant hostParticipant = Participant.builder()
                .event(savedEvent)
                .user(host)
                .isHoster(true)
                .rsvpStatus(RsvpStatus.JOINED)
                .build();

        participantRepository.save(hostParticipant);

        int currentParticipants = 1;
        return eventMapper.toDto(savedEvent, currentParticipants, true);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto getEventById(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        boolean isUserJoined = false;
        if (username != null) {
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            isUserJoined = participantRepository.existsByEventIdAndUserIdAndRsvpStatus(
                    eventId, user.getId(), RsvpStatus.JOINED);
        }

        int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(eventId, RsvpStatus.JOINED);
        return eventMapper.toDto(event, currentParticipants, isUserJoined);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllActiveEvents(String username) {
        Long userId = null;
        if (username != null) {
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            userId = user.getId();
        }

        List<Event> events = eventRepository.findByStatusAndDateTimeAfter(
                EventStatus.ACTIVE, LocalDateTime.now());

        final Long finalUserId = userId;
        return events.stream()
                .map(event -> {
                    boolean isUserJoined = finalUserId != null &&
                            participantRepository.existsByEventIdAndUserIdAndRsvpStatus(
                                    event.getId(), finalUserId, RsvpStatus.JOINED);
                    int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(
                            event.getId(), RsvpStatus.JOINED);
                    return eventMapper.toDto(event, currentParticipants, isUserJoined);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getActiveEventsByType(EventType type, String username) {
        Long userId = null;
        if (username != null) {
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            userId = user.getId();
        }

        List<Event> events = eventRepository.findByStatusAndTypeAndDateTimeAfter(
                EventStatus.ACTIVE, type, LocalDateTime.now());

        final Long finalUserId = userId;
        return events.stream()
                .map(event -> {
                    boolean isUserJoined = finalUserId != null &&
                            participantRepository.existsByEventIdAndUserIdAndRsvpStatus(
                                    event.getId(), finalUserId, RsvpStatus.JOINED);
                    int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(
                            event.getId(), RsvpStatus.JOINED);
                    return eventMapper.toDto(event, currentParticipants, isUserJoined);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventResponseDto joinEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Проверяем, не присоединился ли пользователь уже
        if (participantRepository.existsByEventIdAndUserIdAndRsvpStatus(
                eventId, user.getId(), RsvpStatus.JOINED)) {
            throw new ResourceAlreadyExistsException("You have already joined this event");
        }

        // Проверяем лимит участников
        if (event.getMaxParticipants() != null) {
            int currentCount = participantRepository.countByEventIdAndRsvpStatus(
                    eventId, RsvpStatus.JOINED);
            if (currentCount >= event.getMaxParticipants()) {
                throw new ResourceAlreadyExistsException("Event is full");
            }
        }

        // Проверяем, не в прошлом ли событие
        if (event.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot join past event");
        }

        // Проверяем статус события
        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new IllegalStateException("Event is not active");
        }

        Participant participant = Participant.builder()
                .event(event)
                .user(user)
                .isHoster(false)
                .rsvpStatus(RsvpStatus.JOINED)
                .build();

        participantRepository.save(participant);

        int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(eventId, RsvpStatus.JOINED);
        return eventMapper.toDto(event, currentParticipants, true);
    }

    @Override
    @Transactional
    public EventResponseDto leaveEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Participant participant = participantRepository.findByEventIdAndUserId(eventId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You are not a participant of this event"));

        // Хост не может покинуть своё событие
        if (participant.getIsHoster()) {
            throw new IllegalStateException("Host cannot leave their own event. Cancel it instead.");
        }

        participant.setRsvpStatus(RsvpStatus.LEFT);
        participantRepository.save(participant);

        int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(eventId, RsvpStatus.JOINED);
        return eventMapper.toDto(event, currentParticipants, false);
    }

    @Override
    @Transactional
    public EventResponseDto cancelEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Проверяем, что пользователь - хост этого ивента
        if (!event.getHost().getId().equals(user.getId())) {
            throw new IllegalStateException("Only the host can cancel the event");
        }

        // Проверяем статус
        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new IllegalStateException("Can only cancel active events");
        }

        event.setStatus(EventStatus.CANCELED);
        Event updatedEvent = eventRepository.save(event);

        int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(eventId, RsvpStatus.JOINED);
        return eventMapper.toDto(updatedEvent, currentParticipants, true);
    }

    @Override
    @Transactional
    public EventResponseDto completeEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Проверяем, что пользователь - хост этого ивента
        if (!event.getHost().getId().equals(user.getId())) {
            throw new IllegalStateException("Only the host can complete the event");
        }

        // Проверяем статус
        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new IllegalStateException("Can only complete active events");
        }

        // Проверяем, что событие в прошлом
        if (event.getDateTime().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot complete a future event");
        }

        event.setStatus(EventStatus.DONE);
        Event updatedEvent = eventRepository.save(event);

        int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(eventId, RsvpStatus.JOINED);
        return eventMapper.toDto(updatedEvent, currentParticipants, true);
    }

    @Override
    @Transactional
    public EventResponseDto kickParticipant(Long eventId, Long participantUserId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        User host = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Проверяем, что пользователь - хост этого ивента
        if (!event.getHost().getId().equals(host.getId())) {
            throw new IllegalStateException("Only the host can kick participants");
        }

        // Проверяем статус события
        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new IllegalStateException("Can only kick participants from active events");
        }

        // Находим участника
        Participant participant = participantRepository.findByEventIdAndUserId(eventId, participantUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found in this event"));

        // Хост не может кикнуть сам себя
        if (participant.getIsHoster()) {
            throw new IllegalStateException("Host cannot kick themselves");
        }

        // Проверяем, что участник присоединён
        if (participant.getRsvpStatus() != RsvpStatus.JOINED) {
            throw new IllegalStateException("Can only kick joined participants");
        }

        participant.setRsvpStatus(RsvpStatus.KICKED);
        participantRepository.save(participant);

        int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(eventId, RsvpStatus.JOINED);
        return eventMapper.toDto(event, currentParticipants, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipantResponseDto> getEventParticipants(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event not found");
        }

        List<Participant> participants = participantRepository.findByEventIdAndRsvpStatus(
                eventId, RsvpStatus.JOINED);

        return participants.stream()
                .map(participantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getUserEvents(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Participant> participants = participantRepository.findByUserId(user.getId());

        return participants.stream()
                .filter(p -> p.getRsvpStatus() == RsvpStatus.JOINED)
                .map(p -> {
                    int currentParticipants = participantRepository.countByEventIdAndRsvpStatus(
                            p.getEvent().getId(), RsvpStatus.JOINED);
                    return eventMapper.toDto(p.getEvent(), currentParticipants, true);
                })
                .collect(Collectors.toList());
    }
}
