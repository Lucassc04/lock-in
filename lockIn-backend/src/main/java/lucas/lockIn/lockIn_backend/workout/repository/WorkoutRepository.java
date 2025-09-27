package lucas.lockIn.lockIn_backend.workout.repository;

import lucas.lockIn.lockIn_backend.workout.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout,Long> {
}
