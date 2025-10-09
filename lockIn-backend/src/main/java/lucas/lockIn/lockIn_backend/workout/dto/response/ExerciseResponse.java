package lucas.lockIn.lockIn_backend.workout.dto.response;

import lucas.lockIn.lockIn_backend.workout.entity.Muscle;

import java.util.Set;

public record ExerciseResponse(Long id, String name, Set<Muscle> primaryMuscles, Set<Muscle> secondaryMuscles, String description) {
}
