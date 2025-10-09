package lucas.lockIn.lockIn_backend.workout.dto.request;

import jakarta.annotation.Nullable;

import java.util.Set;

public record ExecutedWorkoutPlanRequest(Set<WorkingSeriesRequest> workingSeries, @Nullable Set<WarmupSeriesRequest> warmupSeries, @Nullable String notes) {
}
