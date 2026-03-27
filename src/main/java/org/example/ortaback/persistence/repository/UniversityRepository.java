package org.example.ortaback.persistence.repository;

import org.example.ortaback.persistence.models.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    
    Optional<University> findByName(String name);
    
    Optional<University> findByDomainPattern(String domainPattern);
    
    boolean existsByDomainPattern(String domainPattern);
}
