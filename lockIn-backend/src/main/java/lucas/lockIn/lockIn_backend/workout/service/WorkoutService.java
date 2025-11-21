package lucas.lockIn.lockIn_backend.workout.service;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.CurrentWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExecutedWorkoutRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.WarmupSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkingSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutResponse;
import lucas.lockIn.lockIn_backend.workout.entity.*;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.exceptions.WorkoutUnfinished;
import lucas.lockIn.lockIn_backend.workout.mapper.WorkoutMapper;
import lucas.lockIn.lockIn_backend.workout.repository.ExecutedWorkoutRepository;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanRepository;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExecutedWorkoutRepository executedWorkoutRepository;
    private final ExerciseService exerciseService;
    private final WorkoutMapper mapper;
    private final UserService userService;

    public WorkoutResponse findById(Long id) {
        return mapper.toResponse(workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id)));
    }

    public List<WorkoutResponse> findAll() {
        return mapper.toResponseList(workoutRepository.findAll());
    }

    public WorkoutResponse findByIdForUser(Long id, Long userId) {
        return mapper.toResponse(workoutRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id)));
    }

    public List<WorkoutResponse> findAllForUser(Long userId) {
        return mapper.toResponseList(workoutRepository.findAllByUserId(userId));
    }


    public WorkoutResponse postWorkout(WorkoutRequest workoutRequest, Long workoutPlanId, Long userId) {
        Workout workout = new  Workout();

        //if the user choose a plan to follow during the workout
        if (workoutPlanId != null) {
            WorkoutPlan workoutPlan = workoutPlanRepository.findByIdAndUsers_Id(workoutPlanId, userId)
                    .orElseThrow(() -> new EntityNotFoundException("Workout Plan", workoutPlanId));
            workout.setWorkoutPlan(workoutPlan);
        }
        User user = userService.findById(userId);
        workout.setUser(user);

        //verify if it's well-defined, and set
        ExecutedWorkout executedWorkout = new ExecutedWorkout();
        executedWorkout.setSeries(convertSeriesRequestToEntities(workoutRequest.executedWorkoutRequest()));
        ExecutedWorkout savedExecutedWorkout = executedWorkoutRepository.save(executedWorkout);

        workout.setExecutedWorkout(savedExecutedWorkout);
        workout.setStartTime(workoutRequest.startTime());
        workout.setFinishTime(workoutRequest.finishTime());
        workout.setDuration(Duration.between(workoutRequest.startTime(), workoutRequest.finishTime()));
        workout.setNotes(workoutRequest.notes());

        workout = workoutRepository.save(workout);
        return mapper.toResponse(workout);
    }

    public CurrentWorkoutDTO startWorkout(@Nullable Long workoutPlanId, Long userId) {
        if(userHasOngoingWorkout(userId)){
            throw new WorkoutUnfinished("A workout is still ongoing! Finish before you can start another workout!");
        }
        Workout workout = new Workout();
        //if the user choose a plan to follow during the workout
        if (workoutPlanId != null) {
            WorkoutPlan workoutPlan = workoutPlanRepository.findByIdAndUsers_Id(workoutPlanId, userId)
                    .orElseThrow(() -> new EntityNotFoundException("Workout Plan", workoutPlanId));
            workout.setWorkoutPlan(workoutPlan);
        }
        workout.setStartTime(LocalDateTime.now());

        User user = userService.findById(userId);
        workout.setUser(user);

        workout = workoutRepository.save(workout);
        return mapper.toCurrent(workout);
    }

    public void cancelOngoingWorkout(Long userId) {
        workoutRepository.deleteOngoingWorkout(userId);
    }

    public WorkoutResponse finishWorkout(Long userId, ExecutedWorkoutRequest executedWorkoutRequest) {
        Workout workout = workoutRepository.findOngoingWorkout(userId).
                orElseThrow(() -> new EntityNotFoundException("Ongoing workout by user", userId));

        ExecutedWorkout executedWorkout = new ExecutedWorkout();
        executedWorkout.setSeries(convertSeriesRequestToEntities(executedWorkoutRequest));
        ExecutedWorkout savedExecutedWorkout = executedWorkoutRepository.save(executedWorkout);

        workout.setFinishTime(LocalDateTime.now());
        workout.setDuration(Duration.between(workout.getStartTime(), workout.getFinishTime()));

        workout.setExecutedWorkout(savedExecutedWorkout);

        workout = workoutRepository.save(workout);
        return mapper.toResponse(workout);
    }


    private List<Series> convertSeriesRequestToEntities(ExecutedWorkoutRequest executedWorkoutRequest){
        List<Series> series = executedWorkoutRequest.workingSeries().stream()
                .map(this::fromRequest)
                .collect(Collectors.toList());
        //Warmup Series are optional, if they do exist, add them to the series set.
        if(executedWorkoutRequest.warmupSeries() != null){
            series.addAll(executedWorkoutRequest.warmupSeries().stream()
                    .map(this::fromRequest)
                    .toList());
        }

        return series;
    }


    private WorkingSeries fromRequest(WorkingSeriesRequest request) {
        int series = (request.series() == null) ? 1 : request.series();
        return new WorkingSeries(exerciseService.findByName(request.exerciseName()), series, request.weight(), request.repetitions());
    }

    private WarmupSeries fromRequest(WarmupSeriesRequest request) {
        return new WarmupSeries(exerciseService.findByName(request.exerciseName()), 1, request.weight(), request.repetitions());
    }

    private boolean userHasOngoingWorkout(Long userId) {
        return workoutRepository.findOngoingWorkout(userId).isPresent();
    }
}
