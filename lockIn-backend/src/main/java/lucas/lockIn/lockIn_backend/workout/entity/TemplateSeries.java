package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_set", discriminatorType = DiscriminatorType.STRING)
public class TemplateSeries {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @OneToOne
    protected Exercise exercise;

    protected int series;

    public TemplateSeries() {}

    public TemplateSeries(Exercise exercise, int set) {
        this.exercise = exercise;
        this.series = set;
    }
}
