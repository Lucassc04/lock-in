package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("WARMUP")
public class WarmupSeries extends TemplateSeries {

    public WarmupSeries() {
    }
}
