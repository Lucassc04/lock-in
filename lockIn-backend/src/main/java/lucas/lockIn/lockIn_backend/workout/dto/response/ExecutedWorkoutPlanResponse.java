package lucas.lockIn.lockIn_backend.workout.dto.response;

import java.util.List;

public record ExecutedWorkoutPlanResponse(Long id, List<WorkingSeriesResponse> workingSeries, List<WarmupSeriesResponse> warmupSeries) {
}
