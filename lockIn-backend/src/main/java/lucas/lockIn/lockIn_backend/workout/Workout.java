package lucas.lockIn.lockIn_backend.workout;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    private TemplateWorkout templateWorkout;

    private LocalDateTime finishTime;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;
}
