package lucas.lockIn.lockIn_backend.workout.dto.request;

import java.util.List;

public record WorkoutPlanRequest(String name, List<PlannedSeriesRequest> series) {
}
