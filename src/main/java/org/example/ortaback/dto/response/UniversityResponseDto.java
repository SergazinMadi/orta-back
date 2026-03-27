package org.example.ortaback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityResponseDto {
    
    private Long id;
    private String name;
    private String domainPattern;
}
