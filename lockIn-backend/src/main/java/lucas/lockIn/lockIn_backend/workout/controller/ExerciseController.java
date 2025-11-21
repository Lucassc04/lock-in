package lucas.lockIn.lockIn_backend.workout.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getExercise(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ExerciseResponse exercise = exerciseService.findByIdForUser(id, userPrincipal.getUserId());
        return ResponseEntity.ok(exercise);
    }

    @GetMapping()
    public ResponseEntity<?> getAllExercises(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ExerciseResponse> exerciseList = exerciseService.findAllByCreatorId(userPrincipal.getUserId());
        return ResponseEntity.ok(exerciseList);
    }

    @PostMapping()
    public ResponseEntity<?> createExercise(@RequestBody @Valid ExerciseRequest exerciseRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ExerciseResponse exercise = exerciseService.createExercise(exerciseRequest, userPrincipal.getUserId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exercise.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<?> updateExercise(
            @RequestBody @Valid ExerciseRequest exerciseRequest, @PathVariable Long exerciseId, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ExerciseResponse exercise = exerciseService.updateExercise(exerciseId, exerciseRequest, userPrincipal.getUserId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exercise.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        exerciseService.deleteExercise(id, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }


}
