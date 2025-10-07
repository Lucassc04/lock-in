package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.Entity;

@Entity
public class PlannedSeries extends Series {

    public PlannedSeries() {}

    public PlannedSeries(Exercise exercise, int set) {
        super(exercise, set);
    }

}
