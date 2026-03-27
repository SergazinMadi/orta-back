package org.example.ortaback.mapper;

import org.example.ortaback.dto.response.ParticipantResponseDto;
import org.example.ortaback.persistence.models.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParticipantMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "isHoster", target = "isHoster")
    @Mapping(source = "rsvpStatus", target = "rsvpStatus")
    @Mapping(source = "attendAt", target = "attendAt")
    @Mapping(source = "joinedAt", target = "joinedAt")
    ParticipantResponseDto toDto(Participant participant);
}
