package lucas.lockIn.lockIn_backend.workout.dto.response;

import java.util.List;

public record ExecutedWorkoutResponse(Long id, List<WorkingSeriesResponse> workingSeries, List<WarmupSeriesResponse> warmupSeries) {
}
