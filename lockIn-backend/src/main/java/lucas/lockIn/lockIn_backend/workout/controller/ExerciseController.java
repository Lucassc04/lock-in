package lucas.lockIn.lockIn_backend.workout.controller;

import lucas.lockIn.lockIn_backend.workout.dto.CreateAndUpdateExerciseDTO;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exercise/")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

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
    public ResponseEntity<?> createExercise(@RequestBody CreateAndUpdateExerciseDTO createAndUpdateExerciseDTO) {
        if(createAndUpdateExerciseDTO.name().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Exercise exercise = exerciseService.createExercise(createAndUpdateExerciseDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exercise.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@RequestBody CreateAndUpdateExerciseDTO createAndUpdateExerciseDTO, @PathVariable long id) {
        if(createAndUpdateExerciseDTO.name().isEmpty() || createAndUpdateExerciseDTO.description().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Exercise exercise = exerciseService.updateExercise(id, createAndUpdateExerciseDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exercise.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }


}
