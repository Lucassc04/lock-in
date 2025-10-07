package lucas.lockIn.lockIn_backend.workout.repository;

import lucas.lockIn.lockIn_backend.workout.entity.ExecutedWorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanExecutedRepository extends JpaRepository<ExecutedWorkoutPlan, Integer> {
}
