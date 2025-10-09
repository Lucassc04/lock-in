package lucas.lockIn.lockIn_backend.workout.controller;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutRequest;
import lucas.lockIn.lockIn_backend.workout.dto.CurrentWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExecutedWorkoutPlanRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Workout;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> getWorkout(@PathVariable Long id) {
        WorkoutResponse workout = workoutService.findById(id);
        return  ResponseEntity.ok(workout);
    }

    @GetMapping()
    public ResponseEntity<List<WorkoutResponse>> getAllWorkouts() {
        List<WorkoutResponse> workouts = workoutService.findAll();
        return ResponseEntity.ok(workouts);
    }

    @PostMapping("/start")
    public ResponseEntity<CurrentWorkoutDTO> startWorkout(@RequestParam @Nullable Long workoutPlanId){
        CurrentWorkoutDTO workout = workoutService.startWorkout(workoutPlanId);
        return ResponseEntity.ok(workout);
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<WorkoutResponse> finishWorkout(@PathVariable Long id,
                                                 @RequestBody ExecutedWorkoutPlanRequest executedWorkoutPlanRequest){
        if(executedWorkoutPlanRequest == null){
            return ResponseEntity.badRequest().build();
        }
        WorkoutResponse workout = workoutService.finishWorkout(id, executedWorkoutPlanRequest);
        return ResponseEntity.ok(workout);
    }

    @PostMapping()
    public ResponseEntity<WorkoutResponse> createWorkout(@RequestParam WorkoutRequest workoutRequest,
                                               @RequestParam @Nullable Long workoutPlanId){

        if(workoutRequest == null){
            return ResponseEntity.badRequest().build();
        }
        if(workoutRequest.startTime() ==  null ||  workoutRequest.finishTime() ==  null
                || workoutRequest.executedWorkoutPlanRequest() == null){
            return ResponseEntity.badRequest().build();
        }

        WorkoutResponse workout = workoutService.createWorkout(workoutRequest, workoutPlanId);
        return ResponseEntity.ok(workout);
    }
}
