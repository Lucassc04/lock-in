package lucas.lockIn.lockIn_backend.workout.repository;

import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan,Long> {

    @Query("""
        SELECT wp
        FROM WorkoutPlan wp
        JOIN wp.users u
        WHERE u.id = :userId AND wp.id = :workoutPlanId
""")
    Optional<WorkoutPlan> findByIdAndUserId(@Param("workoutPlanId") Long workoutPlanId, @Param("userId") Long userId);

    @Query("""
        SELECT wp
        FROM WorkoutPlan wp
        JOIN wp.users u
        WHERE u.id = :userId
""")
    Optional<List<WorkoutPlan>> findAllByUserId(@Param("userId") Long userId);
}
