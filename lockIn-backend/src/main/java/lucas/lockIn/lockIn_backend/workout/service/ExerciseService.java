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
    public Exercise getExercise(long id) {
        return  exerciseRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Exercise", id));
    }

    @Transactional
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @Transactional
    public Exercise createExercise(CreateAndUpdateExerciseDTO createAndUpdateExerciseDTO) {
        Exercise exercise = new Exercise();

        exercise.setDescription(createAndUpdateExerciseDTO.description());
        exercise.setName(createAndUpdateExerciseDTO.name());
        exercise.setPrimaryMuscles(createAndUpdateExerciseDTO.primary());

        //Optional operation, primary movers can't be accessory muscles.
        cleanSecondaryMuscles(createAndUpdateExerciseDTO.primary(), createAndUpdateExerciseDTO.secondary());
        exercise.setSecondaryMuscles(createAndUpdateExerciseDTO.secondary());

        return exerciseRepository.save(exercise);
    }
    @Transactional
    public Exercise updateExercise(long id, CreateAndUpdateExerciseDTO newExercise) {

        Exercise exercise = getExercise(id);

        exercise.setDescription(newExercise.description());
        exercise.setName(newExercise.name());

        Set<Muscle> secondaryMuscles =  newExercise.secondary();
        Set<Muscle> primaryMuscles =  newExercise.primary();
        exercise.setPrimaryMuscles(primaryMuscles);

        cleanSecondaryMuscles(newExercise.primary(), newExercise.secondary());
        exercise.setSecondaryMuscles(secondaryMuscles);

        return exerciseRepository.save(exercise);
    }

    public void deleteExercise(long id) {
        exerciseRepository.deleteById(id);
    }

    private void cleanSecondaryMuscles(Set<Muscle> primaryMuscles, Set<Muscle> secondaryMuscles) {
        secondaryMuscles.removeAll(primaryMuscles);
    }
}
