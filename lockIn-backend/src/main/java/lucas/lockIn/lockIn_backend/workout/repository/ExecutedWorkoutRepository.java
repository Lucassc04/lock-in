package lucas.lockIn.lockIn_backend.workout.repository;

import lucas.lockIn.lockIn_backend.workout.entity.ExecutedWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutedWorkoutRepository extends JpaRepository<ExecutedWorkout, Integer> {
}
