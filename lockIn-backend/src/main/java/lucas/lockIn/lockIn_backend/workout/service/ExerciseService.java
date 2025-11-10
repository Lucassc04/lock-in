package lucas.lockIn.lockIn_backend.workout.service;


import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.exceptions.OwnershipError;
import lucas.lockIn.lockIn_backend.workout.mapper.ExerciseMapperImpl;
import lucas.lockIn.lockIn_backend.workout.repository.ExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public ExerciseResponse findByIdForUser(Long exerciseId, Long userId) {
       return mapper.toResponseDto(exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", exerciseId)));
    }

    @Transactional
    public List<ExerciseResponse> findAllByCreatorId(Long userId) {
        return mapper.toResponseDto(exerciseRepository.findAllByCreatorId(userId));
    }

    @Transactional
    public Exercise findByName(String name) {
        return exerciseRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", name));
    }

    @Transactional
    public ExerciseResponse createExercise(ExerciseRequest exerciseDTO, Long userId) {
        Exercise exercise = new Exercise();

        exercise.setDescription(exerciseDTO.description());
        exercise.setName(exerciseDTO.name());
        exercise.setPrimaryMuscles(exerciseDTO.primaryMuscles());

        if(exerciseDTO.secondaryMuscles() != null) {
            exerciseDTO.secondaryMuscles().removeAll(exerciseDTO.primaryMuscles());
            exercise.setSecondaryMuscles(exerciseDTO.secondaryMuscles());
        }
        //Update user and add creator
        User user = userService.findById(userId);
        exercise.setCreator(user);

        Exercise createdExercise = exerciseRepository.save(exercise);

        return mapper.toResponseDto(createdExercise);
    }

    @Transactional
    public ExerciseResponse updateExercise(Long exerciseId, ExerciseRequest newExercise, Long userId) {

        Exercise exercise = findEntity(exerciseId, userId);

        exercise.setName(newExercise.name());
        exercise.setDescription(newExercise.description());

        exercise.setPrimaryMuscles(newExercise.primaryMuscles());

        if(newExercise.secondaryMuscles() != null) {
            newExercise.secondaryMuscles().removeAll(newExercise.primaryMuscles());
            exercise.setSecondaryMuscles(newExercise.secondaryMuscles());
        }

        Exercise updated = exerciseRepository.save(exercise);
        return mapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteExercise(Long exerciseId, Long userId) {
        Exercise exercise = findEntity(exerciseId, userId);
        if(!exercise.getCreator().getId().equals(userId)){
            throw new OwnershipError("Exercise does not belong to the user");
        }
        exerciseRepository.deleteById(exerciseId);
    }

    private Exercise findEntity(Long exerciseId, Long userId) {
        return exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", exerciseId));
    }
}
