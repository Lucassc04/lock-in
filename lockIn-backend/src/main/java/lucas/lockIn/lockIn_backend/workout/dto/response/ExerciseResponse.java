package lucas.lockIn.lockIn_backend.workout.dto.response;

import jakarta.annotation.Nullable;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;

import java.util.Set;

public record ExerciseResponse(Long id, String name, Set<Muscle> primaryMuscles, @Nullable Set<Muscle> secondaryMuscles, @Nullable String description) {
}
