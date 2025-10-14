package lucas.lockIn.lockIn_backend.workout.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record WorkoutPlanRequest(String name, @NotEmpty List<PlannedSeriesRequest> series) {
}
