package lucas.lockIn.lockIn_backend.workout.dto;

import jakarta.annotation.Nullable;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;

import java.time.LocalDateTime;

public record UnfinishedWorkoutDTO(Long id, LocalDateTime startTime, @Nullable WorkoutPlan workoutPlan) {
}
