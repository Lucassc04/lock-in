package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private WorkoutPlan workoutPlan;

    @OneToOne
    private WorkoutPlanExecuted workoutPlanExecuted;

    private LocalDateTime startTime;

    private LocalDateTime finishTime;

    private Duration duration;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;

    public Workout(){}

    public Workout(WorkoutPlan workoutPlan, LocalDateTime startTime, LocalDateTime finishTime, String notes, Duration duration
    , WorkoutPlanExecuted workoutPlanExecuted) {
        this.workoutPlan = workoutPlan;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.duration = duration;
        this.notes = notes;
        this.workoutPlanExecuted = workoutPlanExecuted;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public WorkoutPlanExecuted getWorkoutPlanExecuted() {
        return workoutPlanExecuted;
    }

    public void setWorkoutPlanExecuted(WorkoutPlanExecuted workoutPlanExecuted) {
        this.workoutPlanExecuted = workoutPlanExecuted;
    }
}
