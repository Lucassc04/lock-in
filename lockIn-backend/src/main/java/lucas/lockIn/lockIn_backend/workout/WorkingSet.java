package lucas.lockIn.lockIn_backend.workout;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("WORKING")
public class WorkingSet extends TemplateSet {

    private Double weight;

    private int repetitions;

    public WorkingSet() {}

    public WorkingSet(Exercise exercise, int set, Double weight, int repetitions) {
        super(exercise, set);
        this.set = set;
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
