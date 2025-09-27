package lucas.lockIn.lockIn_backend.workout.service;

import lucas.lockIn.lockIn_backend.workout.dto.CreateAndUpdateWorkoutPlanDTO;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public WorkoutPlan findById(Long id) {
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout Plan", id));
    }

    public WorkoutPlan createWorkoutPlan(CreateAndUpdateWorkoutPlanDTO createAndUpdateWorkoutPlanDTO) {
        WorkoutPlan workoutPlan = new WorkoutPlan();

        workoutPlan.setName(createAndUpdateWorkoutPlanDTO.name());
        workoutPlan.setSeries(createAndUpdateWorkoutPlanDTO.series());

        return workoutPlanRepository.save(workoutPlan);
    }

    public WorkoutPlan updateWorkoutPlan(Long id, CreateAndUpdateWorkoutPlanDTO createAndUpdateWorkoutPlanDTO) {
        WorkoutPlan workoutPlan = findById(id);

        workoutPlan.setName(createAndUpdateWorkoutPlanDTO.name());
        workoutPlan.setSeries(createAndUpdateWorkoutPlanDTO.series());

        return workoutPlanRepository.save(workoutPlan);
    }

    public void deleteWorkoutPlan(Long id) {
        workoutPlanRepository.deleteById(id);
    }
}
