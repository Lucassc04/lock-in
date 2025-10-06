package lucas.lockIn.lockIn_backend.workout.controller;

import jakarta.annotation.Nullable;
import lucas.lockIn.lockIn_backend.workout.dto.PostWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.UnfinishedWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.WorkoutPlanExecutedDTO;
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
    public ResponseEntity<UnfinishedWorkoutDTO> startWorkout(@RequestParam @Nullable Long workoutPlanId){
        Workout workout = workoutService.startWorkout(workoutPlanId);

        UnfinishedWorkoutDTO unfinishedWorkout = new UnfinishedWorkoutDTO(
                workout.getId(), workout.getStartTime(), workout.getWorkoutPlan());
        return ResponseEntity.ok(unfinishedWorkout);
    }

    @PatchMapping("/finish/{id}")
    public ResponseEntity<Workout> finishWorkout(@PathVariable Long id, @RequestParam @Nullable String notes,
                                                 @RequestParam WorkoutPlanExecutedDTO workoutPlanExecutedDTO){
        if(workoutPlanExecutedDTO == null){
            return ResponseEntity.badRequest().build();
        }
        Workout workout = workoutService.finishWorkout(id, workoutPlanExecutedDTO, notes);
        return ResponseEntity.ok(workout);
    }

    @PostMapping()
    public ResponseEntity<Workout> postWorkout(@RequestParam PostWorkoutDTO postWorkoutDTO,
                                               @RequestParam @Nullable Long workoutPlanId){

        if(postWorkoutDTO == null){
            return ResponseEntity.badRequest().build();
        }
        if(postWorkoutDTO.startTime() ==  null ||  postWorkoutDTO.finishTime() ==  null
                || postWorkoutDTO.workoutPlanExecutedDTO() == null){
            return ResponseEntity.badRequest().build();
        }

        Workout workout = workoutService.postWorkout(postWorkoutDTO, workoutPlanId);
        return ResponseEntity.ok(workout);
    }
}
