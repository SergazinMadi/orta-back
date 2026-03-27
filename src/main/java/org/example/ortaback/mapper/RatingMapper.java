package org.example.ortaback.mapper;

import org.example.ortaback.dto.request.EventCreateRequestDto;
import org.example.ortaback.dto.response.RatingResponseDto;
import org.example.ortaback.persistence.models.Event;
import org.example.ortaback.persistence.models.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.title", target = "eventTitle")
    @Mapping(source = "host.id", target = "hostId")
    @Mapping(source = "host.username", target = "hostName")
    @Mapping(source = "host.fullName", target = "hostFullName")
    @Mapping(source = "reviewer.id", target = "reviewerId")
    @Mapping(source = "reviewer.username", target = "reviewerName")
    @Mapping(source = "reviewer.fullName", target = "reviewerFullName")
    @Mapping(source = "score", target = "score")
    @Mapping(source = "comment", target = "comment")
    @Mapping(source = "createdAt", target = "createdAt")
    RatingResponseDto toDto(Rating rating);
}
