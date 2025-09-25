package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TemplateWorkout templateWorkout;

    private LocalDateTime finishTime;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;

    public Workout(){}

    public Workout(TemplateWorkout templateWorkout, LocalDateTime finishTime, String notes){
        this.templateWorkout = templateWorkout;
        this.finishTime = finishTime;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public TemplateWorkout getTemplateWorkout() {
        return templateWorkout;
    }

    public void setTemplateWorkout(TemplateWorkout templateWorkout) {
        this.templateWorkout = templateWorkout;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
