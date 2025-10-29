package lucas.lockIn.lockIn_backend.workout.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record WorkoutPlanRequest(@NotBlank String name, @NotEmpty List<PlannedSeriesRequest> series) {
}
