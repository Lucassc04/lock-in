package lucas.lockIn.lockIn_backend.workout.dto.response;

import java.time.LocalDateTime;

public record WorkoutResponse(Long id, ExecutedWorkoutPlanResponse executedWorkoutPlan, LocalDateTime startTime, LocalDateTime finishTime) {
}
