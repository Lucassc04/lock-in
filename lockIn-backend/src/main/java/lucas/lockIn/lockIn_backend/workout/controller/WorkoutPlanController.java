package lucas.lockIn.lockIn_backend.workout.controller;

import lucas.lockIn.lockIn_backend.workout.dto.CreateAndUpdateWorkoutPlanDTO;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/workoutPlan")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlan> getWorkoutPlan(@PathVariable Long id){
        if(id == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(workoutPlanService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<WorkoutPlan>> getAllWorkoutPlan(){
        return  ResponseEntity.ok(workoutPlanService.findAll());
    }

    @PostMapping()
    public ResponseEntity<WorkoutPlan> createWorkoutPlan(@RequestBody CreateAndUpdateWorkoutPlanDTO workoutPlan){
        if(workoutPlan == null){
            return ResponseEntity.badRequest().build();
        }
        if(workoutPlan.name() == null || workoutPlan.name().isEmpty()
                || workoutPlan.series() == null || workoutPlan.series().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        WorkoutPlan newWorkoutPlan = workoutPlanService.createWorkoutPlan(workoutPlan);
        URI location =  ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(newWorkoutPlan.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlan> updateWorkoutPlan(@PathVariable Long id, @RequestBody CreateAndUpdateWorkoutPlanDTO workoutPlan){
        if(workoutPlan == null){
            return ResponseEntity.badRequest().build();
        }
        if(workoutPlan.name() == null || workoutPlan.name().isEmpty()
                || workoutPlan.series() == null || workoutPlan.series().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        WorkoutPlan updatedWorkoutPlan = workoutPlanService.updateWorkoutPlan(id, workoutPlan);
        return ResponseEntity.ok(updatedWorkoutPlan);
    }
}
