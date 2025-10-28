package lucas.lockIn.lockIn_backend.auth.dto.request;

import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.Workout;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;

import java.util.List;

public record UserDomainDetailsRequest(List<Exercise> exercises, List<WorkoutPlan> workoutPlans, List<Workout> workouts) {
}
