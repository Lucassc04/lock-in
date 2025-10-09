package lucas.lockIn.lockIn_backend.workout.dto.response;

import java.util.Set;

public record WorkoutPlanResponse(Long id, String name, Set<PlannedSeriesResponse> series) {
}
