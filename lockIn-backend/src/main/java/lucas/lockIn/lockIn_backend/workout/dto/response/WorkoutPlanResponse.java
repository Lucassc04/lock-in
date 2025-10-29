package lucas.lockIn.lockIn_backend.workout.dto.response;

import lucas.lockIn.lockIn_backend.auth.entity.User;

import java.util.Set;

public record WorkoutPlanResponse(Long id, User creator, String name, Set<PlannedSeriesResponse> series) {
}
