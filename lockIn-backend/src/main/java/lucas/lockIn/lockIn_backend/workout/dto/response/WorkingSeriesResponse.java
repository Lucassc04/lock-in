package lucas.lockIn.lockIn_backend.workout.dto.response;

public record WorkingSeriesResponse(ExecutedExerciseResponse exercise, Double weight, Integer repetitions, Integer series) {
}
