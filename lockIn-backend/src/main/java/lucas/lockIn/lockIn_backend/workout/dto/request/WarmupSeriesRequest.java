package lucas.lockIn.lockIn_backend.workout.dto.request;

public record WarmupSeriesRequest(Long exerciseId, Double weight, Integer repetitions,  Integer series) {
}
