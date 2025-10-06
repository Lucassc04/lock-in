package lucas.lockIn.lockIn_backend.workout.dto;

import lucas.lockIn.lockIn_backend.workout.entity.Series;

import java.util.Set;

public record WorkoutPlanExecutedDTO(Set<Series> series) {
}
