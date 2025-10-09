package lucas.lockIn.lockIn_backend.workout.service;


import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.mapper.ExerciseMapperImpl;
import lucas.lockIn.lockIn_backend.workout.repository.ExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapperImpl mapper;

    @Transactional
    public Exercise findById(long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
    }

    @Transactional
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Transactional
    public ExerciseResponse createExercise(ExerciseRequest exerciseDTO) {
        Exercise exercise = new Exercise();

        exercise.setDescription(exerciseDTO.description());
        exercise.setName(exerciseDTO.name());
        exercise.setPrimaryMuscles(exerciseDTO.primaryMuscles());

        //Optional operation, primaryMuscles movers can't be accessory muscles.
        exerciseDTO.secondaryMuscles().removeAll(exerciseDTO.primaryMuscles());
        exercise.setSecondaryMuscles(exerciseDTO.secondaryMuscles());
        exercise = exerciseRepository.save(exercise);

        return mapper.toResponseDto(exercise);
    }
    @Transactional
    public ExerciseResponse updateExercise(long id, ExerciseRequest newExercise) {

        Exercise exercise = findById(id);

        mapper.updateEntityFromDTO(newExercise, exercise);

        //Optional operation, primaryMuscles movers can't be accessory muscles.
        newExercise.secondaryMuscles().removeAll(newExercise.primaryMuscles());
        exercise.setSecondaryMuscles(newExercise.secondaryMuscles());

        Exercise updated = exerciseRepository.save(exercise);
        return mapper.toResponseDto(updated);
    }

    public void deleteExercise(long id) {
        exerciseRepository.deleteById(id);
    }

}
