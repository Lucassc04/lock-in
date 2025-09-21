package lucas.lockIn.lockIn_backend.workout;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("WARMUP")
public class WarmupSet extends TemplateSet {

    public WarmupSet() {
    }
}
