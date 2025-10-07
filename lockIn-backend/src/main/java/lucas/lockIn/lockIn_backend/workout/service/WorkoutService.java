package lucas.lockIn.lockIn_backend.workout.service;

import jakarta.annotation.Nullable;
import lucas.lockIn.lockIn_backend.workout.dto.PostWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.WarmupSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.WorkingSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.ExecutedWorkoutPlanDTO;
import lucas.lockIn.lockIn_backend.workout.entity.*;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanExecutedRepository;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanRepository;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanExecutedRepository workoutPlanExecutedRepository;
    private final ExerciseService exerciseService;

    public WorkoutService(WorkoutRepository workoutRepository,
                          WorkoutPlanRepository workoutPlanRepository,
                          WorkoutPlanExecutedRepository workoutPlanExecutedRepository,
                          ExerciseService exerciseService) {
        this.workoutRepository = workoutRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanExecutedRepository = workoutPlanExecutedRepository;
        this.exerciseService = exerciseService;
    }

    @Transactional
    public Workout findById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id));
    }

    @Transactional
    public List<Workout> findAll() {
        return workoutRepository.findAll();
    }

    @Transactional
    public Workout postWorkout(PostWorkoutDTO postWorkoutDTO, Long workoutPlanId) {
        Workout workout = new  Workout();

        //if the user choose a plan to follow during the workout
        if (workoutPlanId != null) {
            WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                    .orElseThrow(() -> new EntityNotFoundException("Workout Plan", workoutPlanId));
            workout.setWorkoutPlan(workoutPlan);
        }

        //verify if it's well-defined, and set
        ExecutedWorkoutPlan executedWorkoutPlan = new ExecutedWorkoutPlan();
        executedWorkoutPlan.setSeries(convertSeriesRequestToEntities(postWorkoutDTO.executedWorkoutPlanDTO()));
        ExecutedWorkoutPlan savedExecutedWorkoutPlan = workoutPlanExecutedRepository.save(executedWorkoutPlan);

        workout.setWorkoutPlanExecuted(savedExecutedWorkoutPlan);
        workout.setStartTime(postWorkoutDTO.startTime());
        workout.setFinishTime(postWorkoutDTO.finishTime());
        workout.setDuration(Duration.between(postWorkoutDTO.startTime(), postWorkoutDTO.finishTime()));

        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout startWorkout(@Nullable Long workoutPlanId) {
        Workout workout = new Workout();
        //if the user choose a plan to follow during the workout
        if (workoutPlanId != null) {
            WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                    .orElseThrow(() -> new EntityNotFoundException("Workout Plan", workoutPlanId));
            workout.setWorkoutPlan(workoutPlan);
        }

        workout.setStartTime(LocalDateTime.now());
        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout finishWorkout(Long id, ExecutedWorkoutPlanDTO executedWorkoutPlanDTO, String notes) {
        Workout workout = findById(id);

        ExecutedWorkoutPlan executedWorkoutPlan = new ExecutedWorkoutPlan();
        executedWorkoutPlan.setSeries(convertSeriesRequestToEntities(executedWorkoutPlanDTO));
        ExecutedWorkoutPlan savedExecutedWorkoutPlan = workoutPlanExecutedRepository.save(executedWorkoutPlan);

        workout.setFinishTime(LocalDateTime.now());
        workout.setDuration(Duration.between(workout.getStartTime(), workout.getFinishTime()));

        workout.setWorkoutPlanExecuted(savedExecutedWorkoutPlan);
        workout.setNotes(notes);

        return workoutRepository.save(workout);
    }

    /**
     * Converts series requests from a WorkoutPlanExecutedDTO into Series entities.
     * <p>
     * This method processes both working series and warmup series requests (if present),
     * retrieves the associated exercises, and creates the corresponding Series entities.
     * The result is a unified set containing all executed series from the workout.
     * </p>
     *
     * @param executedWorkoutPlanDTO the DTO containing working and warmup series requests
     * @return a set of all Series entities executed in the workout
     * @throws EntityNotFoundException if any exercise ID is not found
     */
    private Set<Series> convertSeriesRequestToEntities(ExecutedWorkoutPlanDTO executedWorkoutPlanDTO){
        Set<Series> series = executedWorkoutPlanDTO.workingSeries().stream()
                .map(this::fromRequest)
                .collect(Collectors.toSet());
        //Warmup Series are optional, if they do exist, add them to the series set.
        if(executedWorkoutPlanDTO.warmupSeries() != null){
            series.addAll(executedWorkoutPlanDTO.warmupSeries().stream()
                    .map(this::fromRequest)
                    .collect(Collectors.toSet()));
        }

        return series;
    }


    /**
     * Creates a WorkingSeries entity from a request DTO.
     * <p>
     * Constructs a new working series instance with the exercise reference and
     * the weight and repetition values from the request.
     * </p>
     *
     * @param request the DTO containing exercise id, weight and repetition data
     * @return a new WorkingSeries instance
     */
    private WorkingSeries fromRequest(WorkingSeriesRequest request) {
        return new WorkingSeries(exerciseService.findById(request.exerciseId()), 1, request.weight(), request.repetitions());
    }

    /**
     * Creates a WarmupSeries entity from a request DTO.
     * <p>
     * Constructs a new warmup series instance with the exercise reference and
     * the weight and repetition values from the request.
     * </p>
     *
     * @param request the DTO containing exercise id, weight and repetition data
     * @return a new WarmupSeries instance
     */
    private WarmupSeries fromRequest(WarmupSeriesRequest request) {
        return new WarmupSeries(exerciseService.findById(request.exerciseId()), 1, request.weight(), request.repetitions());
    }
}
