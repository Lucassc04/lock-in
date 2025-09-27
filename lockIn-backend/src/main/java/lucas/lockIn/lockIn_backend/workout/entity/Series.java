package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;

@Embeddable
public abstract class Series {

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

    public Long getId() {
        return id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }
}
