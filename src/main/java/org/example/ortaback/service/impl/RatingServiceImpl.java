package org.example.ortaback.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ortaback.dto.request.RatingCreateRequestDto;
import org.example.ortaback.dto.response.RatingResponseDto;
import org.example.ortaback.exception.ResourceAlreadyExistsException;
import org.example.ortaback.exception.ResourceNotFoundException;
import org.example.ortaback.mapper.RatingMapper;
import org.example.ortaback.persistence.models.Event;
import org.example.ortaback.persistence.models.Participant;
import org.example.ortaback.persistence.models.Rating;
import org.example.ortaback.persistence.models.User;
import org.example.ortaback.persistence.models.enums.EventStatus;
import org.example.ortaback.persistence.models.enums.RsvpStatus;
import org.example.ortaback.persistence.repository.EventRepository;
import org.example.ortaback.persistence.repository.ParticipantRepository;
import org.example.ortaback.persistence.repository.RatingRepository;
import org.example.ortaback.persistence.repository.UserRepository;
import org.example.ortaback.service.RatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final RatingMapper ratingMapper;

    @Override
    @Transactional
    public RatingResponseDto rateHost(RatingCreateRequestDto requestDto, String username) {
        User reviewer = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Event event = eventRepository.findById(requestDto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        // Проверяем, что событие завершено
        if (event.getStatus() != EventStatus.DONE) {
            throw new IllegalStateException("Can only rate completed events");
        }

        // Проверяем, что пользователь был участником этого события
        Participant participant = participantRepository.findByEventIdAndUserId(event.getId(), reviewer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You were not a participant of this event"));

        if (participant.getRsvpStatus() != RsvpStatus.JOINED) {
            throw new IllegalStateException("Only joined participants can rate");
        }

        // Проверяем, что пользователь не оценивает сам себя
        if (event.getHost().getId().equals(reviewer.getId())) {
            throw new IllegalStateException("Cannot rate yourself");
        }

        // Проверяем, не оценил ли пользователь уже этот ивент
        if (ratingRepository.existsByEventIdAndReviewerId(event.getId(), reviewer.getId())) {
            throw new ResourceAlreadyExistsException("You have already rated this event");
        }

        Rating rating = Rating.builder()
                .event(event)
                .host(event.getHost())
                .reviewer(reviewer)
                .score(requestDto.getScore())
                .comment(requestDto.getComment())
                .build();

        Rating savedRating = ratingRepository.save(rating);

        // Обновляем trust score хоста
        updateHostTrustScore(event.getHost().getId());

        return ratingMapper.toDto(savedRating);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponseDto> getHostRatings(Long hostId) {
        if (!userRepository.existsById(hostId)) {
            throw new ResourceNotFoundException("Host not found");
        }

        List<Rating> ratings = ratingRepository.findByHostId(hostId);

        return ratings.stream()
                .map(ratingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getHostAverageRating(Long hostId) {
        if (!userRepository.existsById(hostId)) {
            throw new ResourceNotFoundException("Host not found");
        }

        Double average = ratingRepository.calculateAverageRatingForHost(hostId);
        return average != null ? average : 0.0;
    }

    private void updateHostTrustScore(Long hostId) {
        Double averageRating = ratingRepository.calculateAverageRatingForHost(hostId);
        if (averageRating != null) {
            User host = userRepository.findById(hostId)
                    .orElseThrow(() -> new ResourceNotFoundException("Host not found"));
            host.setTrustScore(averageRating.floatValue());
            userRepository.save(host);
        }
    }
}
