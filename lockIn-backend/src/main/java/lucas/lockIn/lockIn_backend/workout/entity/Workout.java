package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private WorkoutPlan workoutPlan;

    @OneToOne
    private ExecutedWorkoutPlan executedWorkoutPlan;

    private LocalDateTime startTime;

    private LocalDateTime finishTime;

    private Duration duration;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;

    public Workout(){}

}
