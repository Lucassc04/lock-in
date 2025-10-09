package lucas.lockIn.lockIn_backend.workout.dto.request;

import jakarta.annotation.Nullable;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;

import java.util.Set;

public record ExerciseRequest(String name, Set<Muscle> primaryMuscles, Set<Muscle> secondaryMuscles, @Nullable String description) {
}
