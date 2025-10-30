package lucas.lockIn.lockIn_backend.workout.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutRequest;
import lucas.lockIn.lockIn_backend.workout.dto.CurrentWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExecutedWorkoutPlanRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutResponse;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> getWorkout(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserPrincipal user) {
        WorkoutResponse workout = workoutService.findByIdForUser(id, user.getUserId());
        return  ResponseEntity.ok(workout);
    }

    @GetMapping()
    public ResponseEntity<List<WorkoutResponse>> getAllWorkouts(@AuthenticationPrincipal UserPrincipal user) {
        List<WorkoutResponse> workouts = workoutService.findAllForUser(user.getUserId());
        return ResponseEntity.ok(workouts);
    }

    @PostMapping("/start")
    public ResponseEntity<CurrentWorkoutDTO> startWorkout(@RequestParam @Nullable Long workoutPlanId,
                                                          @AuthenticationPrincipal UserPrincipal user) {
        CurrentWorkoutDTO workout = workoutService.startWorkout(workoutPlanId, user.getUserId());
        return ResponseEntity.ok(workout);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelWorkout(@AuthenticationPrincipal UserPrincipal user) {
        workoutService.cancelOngoingWorkout(user.getUserId());
        return ResponseEntity.ok("Ongoing workout was deleted successfully");
    }

    @PatchMapping("/finish")
    public ResponseEntity<WorkoutResponse> finishWorkout(@RequestBody @Valid ExecutedWorkoutPlanRequest executedWorkoutPlanRequest,
                                                         @AuthenticationPrincipal UserPrincipal user) {
        WorkoutResponse workout = workoutService.finishWorkout(user.getUserId(), executedWorkoutPlanRequest);
        return ResponseEntity.ok(workout);
    }

    @PostMapping()
    public ResponseEntity<WorkoutResponse> postWorkout(@RequestBody @Valid WorkoutRequest workoutRequest,
                                                       @RequestParam @Nullable Long workoutPlanId,
                                                       @AuthenticationPrincipal UserPrincipal user) {
        WorkoutResponse workout = workoutService.postWorkout(workoutRequest, workoutPlanId, user.getUserId());
        return ResponseEntity.ok(workout);
    }
}
