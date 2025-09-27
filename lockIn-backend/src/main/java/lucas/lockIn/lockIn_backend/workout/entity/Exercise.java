package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @Column(nullable = false)
    private Set<Muscle> primaryMuscles;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<Muscle> secondaryMuscles;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    public Exercise(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Exercise(){
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Muscle> getPrimaryMuscle() {
        return primaryMuscles;
    }

    public void setPrimaryMuscles(Set<Muscle> primaryMuscle) {
        this.primaryMuscles = primaryMuscle;
    }

    public Set<Muscle> getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public void setSecondaryMuscles(Set<Muscle> secondaryMuscles) {
        this.secondaryMuscles = secondaryMuscles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
