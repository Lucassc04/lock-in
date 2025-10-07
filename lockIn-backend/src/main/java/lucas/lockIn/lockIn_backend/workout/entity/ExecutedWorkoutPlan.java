package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;

import java.util.Set;

/**
 * This class represents a workout plan executed during the workout itself,
 * which means it acts as an already executed state.
 */
@Entity
public class ExecutedWorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Series> series;

    public ExecutedWorkoutPlan(){}

    public ExecutedWorkoutPlan(Set<Series> series) {
        this.series = series;
    }

    public Long getId() {
        return id;
    }

    public Set<Series> getSeries() {
        return series;
    }

    public void setSeries(Set<Series> series) {
        this.series = series;
    }
}
