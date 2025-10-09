package lucas.lockIn.lockIn_backend.workout.controller;

import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.service.ExerciseService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Exercise> getExercise(@PathVariable long id) {
        Exercise exercise = exerciseService.findById(id);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping()
    public ResponseEntity<List<Exercise>> getAllExercises() {
        List<Exercise> exerciseList = exerciseService.findAll();
        return ResponseEntity.ok(exerciseList);
    }

    @PostMapping()
    public ResponseEntity<?> createExercise(@RequestBody ExerciseRequest exerciseRequest) {
        if(exerciseRequest.name().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        ExerciseResponse exercise = exerciseService.createExercise(exerciseRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exercise.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@RequestBody ExerciseRequest exerciseRequest, @PathVariable long id) {
        if(exerciseRequest.name().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        ExerciseResponse exercise = exerciseService.updateExercise(id, exerciseRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exercise.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }


}
