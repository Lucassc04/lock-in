package lucas.lockIn.lockIn_backend.workout.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutPlanRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutPlanResponse;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlan(@PathVariable Long id,
                                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(workoutPlanService.findByIdForUser(id, userPrincipal.getUserId()));
    }

    @GetMapping()
    public ResponseEntity<List<WorkoutPlanResponse>> getAllWorkoutPlan(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return  ResponseEntity.ok(workoutPlanService.findAllForUser(userPrincipal.getUserId()));
    }

    @PostMapping()
    public ResponseEntity<WorkoutPlanResponse> createWorkoutPlan(@RequestBody @Valid WorkoutPlanRequest workoutPlan,
                                                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {

        WorkoutPlanResponse newWorkoutPlan = workoutPlanService.createWorkoutPlan(userPrincipal.getUserId(), workoutPlan);
        URI location =  ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(newWorkoutPlan.id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> updateWorkoutPlan(@PathVariable Long id,
                                                                 @RequestBody @Valid WorkoutPlanRequest workoutPlan,
                                                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        WorkoutPlanResponse updatedWorkoutPlan = workoutPlanService.updateWorkoutPlan(userPrincipal.getUserId(), id, workoutPlan);
        return ResponseEntity.ok(updatedWorkoutPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutPlan(@PathVariable Long id,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {

        workoutPlanService.deleteWorkoutPlan(userPrincipal.getUserId(), id);
        return ResponseEntity.ok().build();
    }
}
