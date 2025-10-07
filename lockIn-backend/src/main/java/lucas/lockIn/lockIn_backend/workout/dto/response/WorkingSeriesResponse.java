package lucas.lockIn.lockIn_backend.workout.dto.response;

import java.time.LocalDateTime;

public record WorkingSeriesResponse(String exerciseName, Double weight, Integer repetitions) {
}
