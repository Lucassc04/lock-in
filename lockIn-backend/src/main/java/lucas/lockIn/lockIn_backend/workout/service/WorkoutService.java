package lucas.lockIn.lockIn_backend.workout.service;

import jakarta.annotation.Nullable;
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
    public Workout finishWorkout(Long id, WorkoutPlanExecuted workoutPlanExecuted, String notes) {
        Workout workout = findById(id);

        workout.setFinishTime(LocalDateTime.now());
        workout.setDuration(Duration.between(workout.getStartTime(), workout.getFinishTime()));
        workout.setWorkoutPlanExecuted(workoutPlanExecuted);

        for(Series series : workoutPlanExecuted.getSeries()) {
            if(series instanceof PlannedSeries){
                throw new WorkoutUnfinished("Workout is unfinished, there are leftover series");
            }
        }
        workoutPlanExecutedRepository.save(workoutPlanExecuted);

        workout.setNotes(notes);

        return workoutRepository.save(workout);
    }

}
