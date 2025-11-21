package lucas.lockIn.lockIn_backend.workout.repository;

import lucas.lockIn.lockIn_backend.workout.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout,Long> {

    @Query("""
            SELECT w
            FROM Workout w
            JOIN w.user u
            WHERE u.id = :userId AND w.id = :workoutId
            """)
    Optional<Workout> findByIdAndUserId(@Param("workoutId") Long workoutId,
                                        @Param("userId") Long userId);

    List<Workout> findAllByUserId(Long userId);

    @Query("""
       SELECT w
       FROM Workout w
       JOIN w.user u
       WHERE w.user.id = :userId AND w.finishTime IS NULL
        """)
    Optional<Workout> findOngoingWorkout(Long userId);

    @Modifying
    @Query("""
       DELETE FROM Workout w
       WHERE w.user.id = :userId AND w.finishTime IS NULL
       """)
    void deleteOngoingWorkout(@Param("userId") Long userId);
}
