package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
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
    @NotEmpty(message = "The exercise must have at least one primary muscle")
    private Set<Muscle> primaryMuscles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<Muscle> secondaryMuscles = new HashSet<>();

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    public Exercise(){}
}
