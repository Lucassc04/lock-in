package lucas.lockIn.lockIn_backend.workout.dto;

import jakarta.annotation.Nullable;

import java.util.Set;

public record ExecutedWorkoutPlanDTO(Set<WorkingSeriesRequest> workingSeries, @Nullable Set<WarmupSeriesRequest> warmupSeries) {
}
