package lucas.lockIn.lockIn_backend.workout.dto;

import java.time.LocalDateTime;

public record PostWorkoutDTO(WorkoutPlanExecutedDTO workoutPlanExecutedDTO, LocalDateTime startTime, LocalDateTime finishTime) {
}
