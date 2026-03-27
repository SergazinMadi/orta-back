package org.example.ortaback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ortaback.dto.request.EventCreateRequestDto;
import org.example.ortaback.dto.response.EventResponseDto;
import org.example.ortaback.dto.response.ParticipantResponseDto;
import org.example.ortaback.persistence.models.User;
import org.example.ortaback.persistence.models.enums.EventType;
import org.example.ortaback.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(
            @Valid @RequestBody EventCreateRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        EventResponseDto response = eventService.createEvent(requestDto, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(
            @PathVariable Long id,
            @AuthenticationPrincipal(errorOnInvalidType = false) User user) {
        String username = user != null ? user.getUsername() : null;
        EventResponseDto response = eventService.getEventById(id, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllActiveEvents(
            @RequestParam(required = false) EventType type,
            @AuthenticationPrincipal(errorOnInvalidType = false) User user) {
        String username = user != null ? user.getUsername() : null;
        List<EventResponseDto> response;
        if (type != null) {
            response = eventService.getActiveEventsByType(type, username);
        } else {
            response = eventService.getAllActiveEvents(username);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<EventResponseDto> joinEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        EventResponseDto response = eventService.joinEvent(id, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<EventResponseDto> leaveEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        EventResponseDto response = eventService.leaveEvent(id, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<EventResponseDto> cancelEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        EventResponseDto response = eventService.cancelEvent(id, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<EventResponseDto> completeEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        EventResponseDto response = eventService.completeEvent(id, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/kick/{participantId}")
    public ResponseEntity<EventResponseDto> kickParticipant(
            @PathVariable Long id,
            @PathVariable Long participantId,
            @AuthenticationPrincipal User user) {
        EventResponseDto response = eventService.kickParticipant(id, participantId, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantResponseDto>> getEventParticipants(
            @PathVariable Long id) {
        List<ParticipantResponseDto> response = eventService.getEventParticipants(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-events")
    public ResponseEntity<List<EventResponseDto>> getUserEvents(
            @AuthenticationPrincipal User user) {
        List<EventResponseDto> response = eventService.getUserEvents(user.getUsername());
        return ResponseEntity.ok(response);
    }
}
