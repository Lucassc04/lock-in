package lucas.lockIn.lockIn_backend.workout.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;

import java.util.Set;

public record ExerciseRequest(@NotBlank String name, @NotEmpty Set<Muscle> primaryMuscles, @Nullable Set<Muscle> secondaryMuscles, @Nullable String description) {
}
