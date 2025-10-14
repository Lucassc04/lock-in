package lucas.lockIn.lockIn_backend.workout.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ExecutedWorkoutPlanRequest(@NotEmpty List<WorkingSeriesRequest> workingSeries, @Nullable List<WarmupSeriesRequest> warmupSeries, @Nullable String notes) {
}
