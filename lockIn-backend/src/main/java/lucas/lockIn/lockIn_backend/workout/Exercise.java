package lucas.lockIn.lockIn_backend.workout;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Muscle primaryMuscle;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<Muscle> secondaryMuscles;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    public Exercise(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Exercise(){
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Muscle getPrimaryMuscle() {
        return primaryMuscle;
    }

    public void setPrimaryMuscle(Muscle primaryMuscle) {
        this.primaryMuscle = primaryMuscle;
    }

    public List<Muscle> getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public void setSecondaryMuscles(List<Muscle> secondaryMuscles) {
        this.secondaryMuscles = secondaryMuscles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
