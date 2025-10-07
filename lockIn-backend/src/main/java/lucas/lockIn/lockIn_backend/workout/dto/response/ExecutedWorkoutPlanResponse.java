package lucas.lockIn.lockIn_backend.workout.dto.response;

import java.util.Set;

public record ExecutedWorkoutPlanResponse(Long id, Set<WorkingSeriesResponse> workingSeries, Set<WarmupSeriesResponse> warmupSeries) {
}
