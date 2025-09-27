package lucas.lockIn.lockIn_backend.workout.dto;

import lucas.lockIn.lockIn_backend.workout.entity.Muscle;

import java.util.Set;

public record CreateAndUpdateExerciseDTO(String name, Set<Muscle> primary, Set<Muscle> secondary, String description) {
}
