package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("WORKING")
public class WorkingSeries extends TemplateSeries {

    private Double weight;

    private int repetitions;

    public WorkingSeries() {}

    public WorkingSeries(Exercise exercise, int series, Double weight, int repetitions) {
        super(exercise, series);
        this.series = series;
        this.weight = weight;
        this.repetitions = repetitions;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}
