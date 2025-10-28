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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Exercise exercise = exerciseService.findByCreatorId(userPrincipal.getUserId(), id);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping()
    public ResponseEntity<List<Exercise>> getAllExercises(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        List<Exercise> exerciseList = exerciseService.findAllByCreatorId(userPrincipal.getUserId());
        return ResponseEntity.ok(exerciseList);
    }

    @PostMapping()
    public ResponseEntity<?> createExercise(@RequestBody @Valid ExerciseRequest exerciseRequest, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        ExerciseResponse exercise = exerciseService.createExercise(userPrincipal.getUserId(), exerciseRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exercise.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<?> updateExercise(@RequestBody @Valid ExerciseRequest exerciseRequest, @PathVariable Long exerciseId, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        ExerciseResponse exercise = exerciseService.updateExercise(userPrincipal.getUserId(), exerciseId, exerciseRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exercise.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        exerciseService.deleteExercise(userPrincipal.getUserId(), id);
        return ResponseEntity.noContent().build();
    }


}
