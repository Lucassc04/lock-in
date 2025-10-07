package lucas.lockIn.lockIn_backend.workout.dto;

import jakarta.annotation.Nullable;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutPlanResponse;

import java.time.LocalDateTime;

public record CurrentWorkoutDTO(Long id, LocalDateTime startTime, @Nullable WorkoutPlanResponse workoutPlan) {
}
