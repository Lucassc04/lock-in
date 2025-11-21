package lucas.lockIn.lockIn_backend.workout.dto.response;

import java.time.Duration;
import java.time.LocalDateTime;

public record WorkoutResponse(Long id, ExecutedWorkoutResponse executedWorkout, LocalDateTime startTime, LocalDateTime finishTime, String notes, Duration duration) {
}
