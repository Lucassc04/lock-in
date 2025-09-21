package lucas.lockIn.lockIn_backend.workout;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class TemplateWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @OneToMany
    private Set<TemplateSet> sets;

}
