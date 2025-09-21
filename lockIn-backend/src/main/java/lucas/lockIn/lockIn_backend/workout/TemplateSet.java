package lucas.lockIn.lockIn_backend.workout;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_set", discriminatorType = DiscriminatorType.STRING)
public class TemplateSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @OneToOne
    protected Exercise exercise;

    protected int set;

    public TemplateSet() {}

    public TemplateSet(Exercise exercise,  int set) {
        this.exercise = exercise;
        this.set = set;
    }
}
