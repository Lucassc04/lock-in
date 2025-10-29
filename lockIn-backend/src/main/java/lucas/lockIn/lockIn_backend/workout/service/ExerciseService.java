package lucas.lockIn.lockIn_backend.workout.service;


import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.exceptions.OwnershipError;
import lucas.lockIn.lockIn_backend.workout.mapper.ExerciseMapperImpl;
import lucas.lockIn.lockIn_backend.workout.repository.ExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserService userService;
    private final ExerciseMapperImpl mapper;

    @Transactional
    public Exercise findById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
    }

    @Transactional
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Transactional
    public Exercise findByIdForUser(Long userId, Long exerciseId) {
       return exerciseRepository.findByIdAndUserId(userId, exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", exerciseId));
    }

    @Transactional
    public List<Exercise> findAllByCreatorId(Long userId) {
        return exerciseRepository.findAllByCreatorId(userId);
    }

    @Transactional
    public ExerciseResponse createExercise(Long userId, ExerciseRequest exerciseDTO) {
        Exercise exercise = new Exercise();

        exercise.setDescription(exerciseDTO.description());
        exercise.setName(exerciseDTO.name());
        exercise.setPrimaryMuscles(exerciseDTO.primaryMuscles());

        //Optional operation, primaryMuscles movers can't be accessory muscles.
        if(exerciseDTO.secondaryMuscles() != null){
            exerciseDTO.secondaryMuscles().removeAll(exerciseDTO.primaryMuscles());
            exercise.setSecondaryMuscles(exerciseDTO.secondaryMuscles());
        }
        //Update user and add creator
        User user = userService.findById(userId);
        exercise.setCreator(user);

        exercise = exerciseRepository.save(exercise);

        return mapper.toResponseDto(exercise);
    }
    @Transactional
    public ExerciseResponse updateExercise(Long userId, Long exerciseId, ExerciseRequest newExercise) {

        Exercise exercise = findByIdForUser(userId, exerciseId);


        mapper.updateEntityFromDTO(newExercise, exercise);

        //Optional operation, primaryMuscles movers can't be accessory muscles.
        if(newExercise.secondaryMuscles() != null){
            newExercise.secondaryMuscles().removeAll(newExercise.primaryMuscles());
            exercise.setSecondaryMuscles(newExercise.secondaryMuscles());
        }

        Exercise updated = exerciseRepository.save(exercise);
        return mapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteExercise(Long userId, Long exerciseId) {
        Exercise exercise = findByIdForUser(userId, exerciseId);
        if(!exercise.getCreator().getId().equals(userId)){
            throw new OwnershipError("Exercise does not belong to the user");
        }
        exerciseRepository.deleteById(exerciseId);
    }
}
