package lucas.lockIn.lockIn_backend.workout.dto.request;

import java.util.Set;

public record WorkoutPlanRequest(String name, Set<PlannedSeriesRequest> series) {
}
