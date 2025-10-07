package lucas.lockIn.lockIn_backend.workout.controller;

import jakarta.annotation.Nullable;
import lucas.lockIn.lockIn_backend.workout.dto.PostWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.CurrentWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.ExecutedWorkoutPlanDTO;
import lucas.lockIn.lockIn_backend.workout.entity.Workout;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }


    @PostMapping("/start")
    public ResponseEntity<CurrentWorkoutDTO> startWorkout(@RequestParam @Nullable Long workoutPlanId){
        Workout workout = workoutService.startWorkout(workoutPlanId);

        CurrentWorkoutDTO unfinishedWorkout = new CurrentWorkoutDTO(
                workout.getId(), workout.getStartTime(), workout.getWorkoutPlan());
        return ResponseEntity.ok(unfinishedWorkout);
    }

    @PatchMapping("/finish/{id}")
    public ResponseEntity<Workout> finishWorkout(@PathVariable Long id, @RequestBody @Nullable String notes,
                                                 @RequestBody ExecutedWorkoutPlanDTO executedWorkoutPlanDTO){
        if(executedWorkoutPlanDTO == null){
            return ResponseEntity.badRequest().build();
        }
        Workout workout = workoutService.finishWorkout(id, executedWorkoutPlanDTO, notes);
        return ResponseEntity.ok(workout);
    }

    @PostMapping()
    public ResponseEntity<Workout> createWorkout(@RequestParam PostWorkoutDTO postWorkoutDTO,
                                               @RequestParam @Nullable Long workoutPlanId){

        if(postWorkoutDTO == null){
            return ResponseEntity.badRequest().build();
        }
        if(postWorkoutDTO.startTime() ==  null ||  postWorkoutDTO.finishTime() ==  null
                || postWorkoutDTO.executedWorkoutPlanDTO() == null){
            return ResponseEntity.badRequest().build();
        }

        Workout workout = workoutService.postWorkout(postWorkoutDTO, workoutPlanId);
        return ResponseEntity.ok(workout);
    }
}
