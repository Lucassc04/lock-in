package lucas.lockIn.lockIn_backend.workout.repository;

import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlanExecuted;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanExecutedRepository extends JpaRepository<WorkoutPlanExecuted, Integer> {
}
