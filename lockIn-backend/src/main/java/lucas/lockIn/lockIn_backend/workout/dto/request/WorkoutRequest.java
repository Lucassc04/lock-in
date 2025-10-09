package lucas.lockIn.lockIn_backend.workout.dto.request;

import java.time.LocalDateTime;

public record WorkoutRequest(ExecutedWorkoutPlanRequest executedWorkoutPlanRequest, LocalDateTime startTime, LocalDateTime finishTime) {
}
