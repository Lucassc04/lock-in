package lucas.lockIn.lockIn_backend.workout.service;

import jakarta.annotation.Nullable;
import lucas.lockIn.lockIn_backend.workout.dto.PostWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.WorkoutPlanExecutedDTO;
import lucas.lockIn.lockIn_backend.workout.entity.*;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.exceptions.WorkoutUnfinished;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanExecutedRepository;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanRepository;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanExecutedRepository workoutPlanExecutedRepository;

    public WorkoutService(WorkoutRepository workoutRepository,  WorkoutPlanRepository workoutPlanRepository, WorkoutPlanExecutedRepository workoutPlanExecutedRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanExecutedRepository = workoutPlanExecutedRepository;
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

        if (workoutPlanId != null) {
            workout.setWorkoutPlan(workoutPlanRepository.findById(workoutPlanId).
                    orElseThrow(() -> new EntityNotFoundException("WorkoutPlan", workoutPlanId)));
        }

        checkWorkoutPlanWasExecuted(postWorkoutDTO.workoutPlanExecutedDTO());
        WorkoutPlanExecuted workoutPlanExecuted = new  WorkoutPlanExecuted();
        workoutPlanExecuted.setSeries(postWorkoutDTO.workoutPlanExecutedDTO().series());
        WorkoutPlanExecuted savedWorkoutPlanExecuted = workoutPlanExecutedRepository.save(workoutPlanExecuted);
        workout.setWorkoutPlanExecuted(savedWorkoutPlanExecuted);

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
    public Workout finishWorkout(Long id, WorkoutPlanExecutedDTO workoutPlanExecutedDTO, String notes) {
        Workout workout = findById(id);

        checkWorkoutPlanWasExecuted(workoutPlanExecutedDTO);

        WorkoutPlanExecuted workoutPlanExecuted = new WorkoutPlanExecuted();
        workoutPlanExecuted.setSeries(workoutPlanExecutedDTO.series());
        WorkoutPlanExecuted savedWorkoutPlanExecuted = workoutPlanExecutedRepository.save(workoutPlanExecuted);

        workout.setFinishTime(LocalDateTime.now());
        workout.setDuration(Duration.between(workout.getStartTime(), workout.getFinishTime()));

        workout.setWorkoutPlanExecuted(savedWorkoutPlanExecuted);
        workout.setNotes(notes);

        return workoutRepository.save(workout);
    }

    private static void checkWorkoutPlanWasExecuted(WorkoutPlanExecutedDTO workoutPlanExecutedDTO) {
        for(Series series : workoutPlanExecutedDTO.series()) {
            if(series instanceof PlannedSeries){
                throw new WorkoutUnfinished("Workout is unfinished, there are leftover series");
            }
        }
    }

}
