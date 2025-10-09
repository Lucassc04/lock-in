package lucas.lockIn.lockIn_backend.workout.service;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.workout.dto.CurrentWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.WarmupSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkingSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExecutedWorkoutPlanRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutResponse;
import lucas.lockIn.lockIn_backend.workout.entity.*;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.mapper.ExecutedWorkoutPlanMapper;
import lucas.lockIn.lockIn_backend.workout.mapper.WorkoutMapper;
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

@AllArgsConstructor
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanExecutedRepository workoutPlanExecutedRepository;
    private final ExerciseService exerciseService;
    private final WorkoutMapper mapper;
    private final ExecutedWorkoutPlanMapper executedWorkoutPlanMapper;

    @Transactional
    public WorkoutResponse findById(Long id) {
        return mapper.toResponse(workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id)));
    }

    @Transactional
    public List<WorkoutResponse> findAll() {
        return mapper.toResponseList(workoutRepository.findAll());
    }

    @Transactional
    public WorkoutResponse createWorkout(WorkoutRequest workoutRequest, Long workoutPlanId) {
        Workout workout = new  Workout();

        //if the user choose a plan to follow during the workout
        if (workoutPlanId != null) {
            WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                    .orElseThrow(() -> new EntityNotFoundException("Workout Plan", workoutPlanId));
            workout.setWorkoutPlan(workoutPlan);
        }

        //verify if it's well-defined, and set
        ExecutedWorkoutPlan executedWorkoutPlan = new ExecutedWorkoutPlan();
        executedWorkoutPlan.setSeries(convertSeriesRequestToEntities(workoutRequest.executedWorkoutPlanRequest()));
        ExecutedWorkoutPlan savedExecutedWorkoutPlan = workoutPlanExecutedRepository.save(executedWorkoutPlan);

        workout.setExecutedWorkoutPlan(savedExecutedWorkoutPlan);
        workout.setStartTime(workoutRequest.startTime());
        workout.setFinishTime(workoutRequest.finishTime());
        workout.setDuration(Duration.between(workoutRequest.startTime(), workoutRequest.finishTime()));

        workout = workoutRepository.save(workout);
        return mapper.toResponse(workout);
    }

    @Transactional
    public CurrentWorkoutDTO startWorkout(@Nullable Long workoutPlanId) {
        Workout workout = new Workout();
        //if the user choose a plan to follow during the workout
        if (workoutPlanId != null) {
            WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                    .orElseThrow(() -> new EntityNotFoundException("Workout Plan", workoutPlanId));
            workout.setWorkoutPlan(workoutPlan);
        }
        workout.setStartTime(LocalDateTime.now());

        workout = workoutRepository.save(workout);
        return mapper.toCurrent(workout);
    }

    @Transactional
    public WorkoutResponse finishWorkout(Long id, ExecutedWorkoutPlanRequest executedWorkoutPlanRequest) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id));

        ExecutedWorkoutPlan executedWorkoutPlan = new ExecutedWorkoutPlan();
        executedWorkoutPlan.setSeries(convertSeriesRequestToEntities(executedWorkoutPlanRequest));
        ExecutedWorkoutPlan savedExecutedWorkoutPlan = workoutPlanExecutedRepository.save(executedWorkoutPlan);

        workout.setFinishTime(LocalDateTime.now());
        workout.setDuration(Duration.between(workout.getStartTime(), workout.getFinishTime()));

        workout.setExecutedWorkoutPlan(savedExecutedWorkoutPlan);
        workout.setNotes(executedWorkoutPlanRequest.notes());

        workout = workoutRepository.save(workout);
        return mapper.toResponse(workout);
    }

    /**
     * Converts series requests from a WorkoutPlanExecutedDTO into Series entities.
     * <p>
     * This method processes both working series and warmup series requests (if present),
     * retrieves the associated exercises, and creates the corresponding Series entities.
     * The result is a unified set containing all executed series from the workout.
     * </p>
     *
     * @param executedWorkoutPlanRequest the DTO containing working and warmup series requests
     * @return a set of all Series entities executed in the workout
     * @throws EntityNotFoundException if any exercise ID is not found
     */
    private Set<Series> convertSeriesRequestToEntities(ExecutedWorkoutPlanRequest executedWorkoutPlanRequest){
        Set<Series> series = executedWorkoutPlanRequest.workingSeries().stream()
                .map(this::fromRequest)
                .collect(Collectors.toSet());
        //Warmup Series are optional, if they do exist, add them to the series set.
        if(executedWorkoutPlanRequest.warmupSeries() != null){
            series.addAll(executedWorkoutPlanRequest.warmupSeries().stream()
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
