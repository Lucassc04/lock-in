package lucas.lockIn.lockIn_backend.workout.dto.request;

import jakarta.annotation.Nullable;

import java.util.List;

public record ExecutedWorkoutPlanRequest(List<WorkingSeriesRequest> workingSeries, @Nullable List<WarmupSeriesRequest> warmupSeries, @Nullable String notes) {
}
