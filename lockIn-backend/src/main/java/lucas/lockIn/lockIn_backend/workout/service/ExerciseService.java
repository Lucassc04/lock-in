package lucas.lockIn.lockIn_backend.workout.service;


import lucas.lockIn.lockIn_backend.workout.dto.CreateAndUpdateExerciseDTO;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.repository.ExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public Exercise findById(long id) {
        return  exerciseRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Exercise", id));
    }

    @Transactional
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Transactional
    public Exercise createExercise(CreateAndUpdateExerciseDTO exerciseDTO) {
        Exercise exercise = new Exercise();

        exercise.setDescription(exerciseDTO.description());
        exercise.setName(exerciseDTO.name());
        exercise.setPrimaryMuscles(exerciseDTO.primary());

        //Optional operation, primary movers can't be accessory muscles.
        exerciseDTO.secondary().removeAll(exerciseDTO.primary());
        exercise.setSecondaryMuscles(exerciseDTO.secondary());

        return exerciseRepository.save(exercise);
    }
    @Transactional
    public Exercise updateExercise(long id, CreateAndUpdateExerciseDTO newExercise) {

        Exercise exercise = findById(id);

        exercise.setDescription(newExercise.description());
        exercise.setName(newExercise.name());

        Set<Muscle> secondaryMuscles =  newExercise.secondary();
        Set<Muscle> primaryMuscles =  newExercise.primary();
        exercise.setPrimaryMuscles(primaryMuscles);

        //Optional operation, primary movers can't be accessory muscles.
        newExercise.secondary().removeAll(newExercise.primary());
        exercise.setSecondaryMuscles(secondaryMuscles);

        return exerciseRepository.save(exercise);
    }

    public void deleteExercise(long id) {
        exerciseRepository.deleteById(id);
    }

}
