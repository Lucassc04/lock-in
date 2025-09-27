package lucas.lockIn.lockIn_backend.workout.repository;

import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan,Long> {
}
