package lucas.lockIn.lockIn_backend.workout.dto;

import java.time.LocalDateTime;

public record PostWorkoutDTO(ExecutedWorkoutPlanDTO executedWorkoutPlanDTO, LocalDateTime startTime, LocalDateTime finishTime) {
}
