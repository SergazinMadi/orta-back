package org.example.ortaback.mapper;

import org.example.ortaback.dto.request.EventCreateRequestDto;
import org.example.ortaback.dto.response.EventResponseDto;
import org.example.ortaback.persistence.models.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "host", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Event toEntity(EventCreateRequestDto dto);

    @Mapping(source = "event.id", target = "id")
    @Mapping(source = "event.host.id", target = "hostId")
    @Mapping(source = "event.host.username", target = "hostName")
    @Mapping(source = "event.host.fullName", target = "hostFullName")
    @Mapping(source = "event.status", target = "status")
    @Mapping(source = "event.title", target = "title")
    @Mapping(source = "event.type", target = "type")
    @Mapping(source = "event.description", target = "description")
    @Mapping(source = "event.placeName", target = "placeName")
    @Mapping(source = "event.address", target = "address")
    @Mapping(source = "event.dateTime", target = "dateTime")
    @Mapping(source = "event.timeDuration", target = "timeDuration")
    @Mapping(source = "event.maxParticipants", target = "maxParticipants")
    @Mapping(source = "event.pricePerPerson", target = "pricePerPerson")
    @Mapping(source = "event.createdAt", target = "createdAt")
    @Mapping(source = "currentParticipants", target = "currentParticipants")
    @Mapping(source = "isUserJoined", target = "isUserJoined")
    EventResponseDto toDto(Event event, Integer currentParticipants, Boolean isUserJoined);
}
