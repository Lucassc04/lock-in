package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lucas.lockIn.lockIn_backend.auth.entity.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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
    @Builder.Default
    private Set<Muscle> primaryMuscles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @Builder.Default
    private Set<Muscle> secondaryMuscles = new HashSet<>();

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name="creator_id")
    private User creator;

    public Exercise(){}
}
