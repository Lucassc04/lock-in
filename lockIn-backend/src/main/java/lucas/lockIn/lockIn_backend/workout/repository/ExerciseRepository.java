package lucas.lockIn.lockIn_backend.workout.repository;

import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByCreatorId(Long userId);

    @Query("""
    SELECT e
    FROM Exercise e
    WHERE e.creator.id = :userId AND e.id = :exerciseId
""")
    Optional<Exercise> findByIdAndUserId(@Param("userId") Long userId,
                                         @Param("exerciseId") Long exerciseId);
}
