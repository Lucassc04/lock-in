package lucas.lockIn.lockIn_backend.workout.dto.response;

public record WarmupSeriesResponse(ExecutedExerciseResponse exercise, Double weight, Integer repetitions, Integer series){}
