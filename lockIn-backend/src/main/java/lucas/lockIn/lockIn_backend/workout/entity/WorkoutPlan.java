package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;
import lombok.*;
import lucas.lockIn.lockIn_backend.auth.entity.User;

import java.util.List;

/**
 * This class represents a workout plan to be followed during a workout.
 * The class has no state, which means it can't represent an executed workout.
 */
@Getter
@Entity
@Setter
@Builder
@AllArgsConstructor
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PlannedSeries> series;

    public WorkoutPlan(){}

    public WorkoutPlan(List<PlannedSeries> series, String name){
        this.series = series;
        this.name = name;
    }

}
