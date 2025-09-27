package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class WorkoutPlanExecuted{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private Set<Series> series;

    public WorkoutPlanExecuted(){}

    public WorkoutPlanExecuted(Set<Series> series) {
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
