package lucas.lockIn.lockIn_backend.workout.dto.response;

import java.util.Set;

public record WorkoutPlanResponse(String name, Set<PlannedSeriesResponse> series) {
}
