package lucas.lockIn.lockIn_backend.workout.dto;

import lucas.lockIn.lockIn_backend.workout.entity.PlannedSeries;

import java.util.Set;

public record CreateAndUpdateWorkoutPlanDTO(String name, Set<PlannedSeries> series) {
}
