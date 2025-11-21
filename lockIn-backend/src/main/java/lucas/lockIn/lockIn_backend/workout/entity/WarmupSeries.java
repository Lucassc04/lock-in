package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
public class WarmupSeries extends Series {

    private Double weight;

    private Integer repetitions;

    public WarmupSeries() {}

    public WarmupSeries(Exercise exercise, int series, Double weight, int repetitions) {
        super(exercise, series);
        this.weight = weight;
        this.repetitions = repetitions;
    }

}
