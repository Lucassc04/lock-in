package lucas.lockIn.lockIn_backend.workout.entity;

import jakarta.persistence.*;
import lombok.*;
import lucas.lockIn.lockIn_backend.auth.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a workout plan to be followed during a workout.
 * The class has no state, which means it can't represent an executed workout.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @Builder.Default
    private List<PlannedSeries> series =  new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "user_workout_plan",
            joinColumns = @JoinColumn(name = "workout_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @ManyToOne()
    @JoinColumn(name="creator_id")
    private User creator;

    public WorkoutPlan(){}

    public WorkoutPlan(List<PlannedSeries> series, String name){
        this.series = series;
        this.name = name;
    }

    public void addUsers(User user){
        if(users == null){
            users = new HashSet<>();
        }
        users.add(user);
    }

}
