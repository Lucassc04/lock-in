package lucas.lockIn.lockIn_backend.workout.service;

import lucas.lockIn.lockIn_backend.workout.dto.CreateAndUpdateWorkoutPlanDTO;
import lucas.lockIn.lockIn_backend.workout.dto.PlannedSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.WarmupSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.PlannedSeries;
import lucas.lockIn.lockIn_backend.workout.entity.WarmupSeries;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.repository.ExerciseRepository;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseService exerciseService;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository,
                              ExerciseService exerciseService) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseService = exerciseService;
    }

    public WorkoutPlan findById(Long id) {
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout Plan", id));
    }

    public List<WorkoutPlan> findAll() {
        return workoutPlanRepository.findAll();
    }

    public WorkoutPlan createWorkoutPlan(CreateAndUpdateWorkoutPlanDTO createAndUpdateWorkoutPlanDTO) {
        WorkoutPlan workoutPlan = new WorkoutPlan();

        workoutPlan.setName(createAndUpdateWorkoutPlanDTO.name());
        workoutPlan.setSeries(converSeriesRequestToEntities(createAndUpdateWorkoutPlanDTO.series()));

        return workoutPlanRepository.save(workoutPlan);
    }


    public WorkoutPlan updateWorkoutPlan(Long id, CreateAndUpdateWorkoutPlanDTO createAndUpdateWorkoutPlanDTO) {
        WorkoutPlan workoutPlan = findById(id);

        workoutPlan.setName(createAndUpdateWorkoutPlanDTO.name());
        workoutPlan.setSeries(converSeriesRequestToEntities(createAndUpdateWorkoutPlanDTO.series()));

        return workoutPlanRepository.save(workoutPlan);
    }

    public void deleteWorkoutPlan(Long id) {
        workoutPlanRepository.deleteById(id);
    }

    /**
     * Converts series requests from a WorkoutPlanExecutedDTO into Series entities.
     * <p>
     * This method processes both working series and warmup series requests (if present),
     * retrieves the associated exercises, and creates the corresponding Series entities.
     * The result is a unified set containing all executed series from the workout.
     * </p>
     *
     * @param series the request containing the exercise id and series
     * @return a set of all Series entities executed in the workout
     * @throws EntityNotFoundException if any exercise ID is not found
     */
    private Set<PlannedSeries> converSeriesRequestToEntities(Set<PlannedSeriesRequest> series) {
        return series.stream().
                map(this::fromRequest).
                collect(Collectors.toSet());
    }

    /**
     * Creates a PlannedSeries entity from a request DTO.
     * <p>
     * Constructs a new planned series instance with the exercise reference and
     * the weight and repetition values from the request.
     * </p>
     *
     * @param request the DTO containing the exercise id and series
     * @return a new PlannedSeries instance
     */
    private PlannedSeries fromRequest(PlannedSeriesRequest request) {
        return new PlannedSeries(exerciseService.findById(request.exerciseId()), request.series());
    }
}
