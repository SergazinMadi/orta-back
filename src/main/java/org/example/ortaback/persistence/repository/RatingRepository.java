package org.example.ortaback.persistence.repository;

import org.example.ortaback.persistence.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findByHostId(Long hostId);
    
    Optional<Rating> findByEventIdAndReviewerId(Long eventId, Long reviewerId);
    
    boolean existsByEventIdAndReviewerId(Long eventId, Long reviewerId);
    
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.host.id = :hostId")
    Double calculateAverageRatingForHost(Long hostId);
    
    int countByHostId(Long hostId);
}
