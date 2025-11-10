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
        WHERE u.id = :userId
""")
    List<WorkoutPlan> findAllByUserId(@Param("userId") Long userId);

    Optional<WorkoutPlan> findByIdAndUsers_Id(Long workoutPlanId, Long userId);
}
