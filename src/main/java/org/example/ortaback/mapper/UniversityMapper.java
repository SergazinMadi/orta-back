package org.example.ortaback.mapper;

import org.example.ortaback.dto.response.UniversityResponseDto;
import org.example.ortaback.persistence.models.University;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UniversityMapper {

    UniversityResponseDto toDto(University university);
}
