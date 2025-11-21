package lucas.lockIn.lockIn_backend.workout.dto.request;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record WorkoutRequest(ExecutedWorkoutRequest executedWorkoutRequest, LocalDateTime startTime, LocalDateTime finishTime, @Nullable String notes) {
}
