package lucas.lockIn.lockIn_backend.workout.dto;

import java.util.Set;

public record CreateAndUpdateWorkoutPlanDTO(String name, Set<PlannedSeriesRequest> series) {
}
