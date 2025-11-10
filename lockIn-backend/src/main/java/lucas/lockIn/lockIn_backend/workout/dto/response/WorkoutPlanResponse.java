package lucas.lockIn.lockIn_backend.workout.dto.response;

import lucas.lockIn.lockIn_backend.auth.dto.response.UserResponse;

import java.util.Set;

public record WorkoutPlanResponse(Long id, UserResponse creator, String name, Set<PlannedSeriesResponse> series) {
}
