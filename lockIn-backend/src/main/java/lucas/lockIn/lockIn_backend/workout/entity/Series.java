package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public abstract class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    protected Exercise exercise;

    protected int series;

    public Series() {}

    public Series(Exercise exercise, int series) {
        this.exercise = exercise;
        this.series = series;
    }

}
