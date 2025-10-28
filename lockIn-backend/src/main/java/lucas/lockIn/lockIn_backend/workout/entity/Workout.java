package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lucas.lockIn.lockIn_backend.auth.entity.User;

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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;

    public Workout(){}

}
