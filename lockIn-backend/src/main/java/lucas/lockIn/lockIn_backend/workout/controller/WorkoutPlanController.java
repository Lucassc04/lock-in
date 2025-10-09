package lucas.lockIn.lockIn_backend.workout.controller;

import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutPlanRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutPlanResponse;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/workoutPlan")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlan(@PathVariable Long id){
        if(id == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(workoutPlanService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<WorkoutPlanResponse>> getAllWorkoutPlan(){
        return  ResponseEntity.ok(workoutPlanService.findAll());
    }

    @PostMapping()
    public ResponseEntity<WorkoutPlanResponse> createWorkoutPlan(@RequestBody WorkoutPlanRequest workoutPlan){
        if(workoutPlan == null){
            return ResponseEntity.badRequest().build();
        }
        if(workoutPlan.name() == null || workoutPlan.name().isEmpty()
                || workoutPlan.series() == null || workoutPlan.series().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        WorkoutPlanResponse newWorkoutPlan = workoutPlanService.createWorkoutPlan(workoutPlan);
        URI location =  ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(newWorkoutPlan.id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> updateWorkoutPlan(@PathVariable Long id, @RequestBody WorkoutPlanRequest workoutPlan){
        if(workoutPlan == null){
            return ResponseEntity.badRequest().build();
        }
        if(workoutPlan.name() == null || workoutPlan.name().isEmpty()
                || workoutPlan.series() == null || workoutPlan.series().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        WorkoutPlanResponse updatedWorkoutPlan = workoutPlanService.updateWorkoutPlan(id, workoutPlan);
        return ResponseEntity.ok(updatedWorkoutPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutPlan(@PathVariable Long id){
        if(id == null){
            return ResponseEntity.badRequest().build();
        }
        workoutPlanService.deleteWorkoutPlan(id);
        return ResponseEntity.ok().build();
    }
}
