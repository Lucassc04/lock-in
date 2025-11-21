package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class WorkingSeries extends Series {

    private Double weight;

    private Integer repetitions;

    public WorkingSeries() {}

    public WorkingSeries(Exercise exercise, int series, Double weight, int repetitions) {
        super(exercise, series);
        this.weight = weight;
        this.repetitions = repetitions;
    }

}
