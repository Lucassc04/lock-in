package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;

import java.util.Set;

/**
 * This class represents a workout plan to be followed during a workout.
 * The class has no state, which means it can't represent an executed workout.
 */
@Entity
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<PlannedSeries> series;

    public WorkoutPlan(){}

    public WorkoutPlan(Set<PlannedSeries> series, String name){
        this.series = series;
        this.name = name;
    }

    public Set<PlannedSeries> getSeries() {
        return series;
    }

    public void setSeries(Set<PlannedSeries> series) {
        this.series = series;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
