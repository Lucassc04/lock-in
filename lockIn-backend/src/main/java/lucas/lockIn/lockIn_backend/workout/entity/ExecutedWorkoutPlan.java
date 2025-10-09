package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a workout plan executed during the workout itself,
 * which means it acts as an already executed state.
 */
@Data
@Entity
public class ExecutedWorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(nullable = false)
    private Set<WorkingSeries> workingSeries = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<WarmupSeries> warmupSeries = new HashSet<>();

    public ExecutedWorkoutPlan(){}

    public void setSeries(Set<Series> series) {
        for (Series s : series) {
            if(s instanceof WarmupSeries wms){
                this.warmupSeries.add(wms);
            }
            if(s instanceof WorkingSeries wks){
                this.workingSeries.add(wks);
            }
        }
    }


}
