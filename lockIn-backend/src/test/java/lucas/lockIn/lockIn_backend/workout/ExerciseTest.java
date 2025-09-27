package lucas.lockIn.lockIn_backend.workout;

import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ExerciseTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    void create_exercise(){
        Exercise exercise = new Exercise();
        Muscle muscle = Muscle.HAMSTRING;

        Set<Muscle> muscles = Set.of(Muscle.CALVES, Muscle.ABS, muscle);

        exercise.setName("Leg Curl");
        exercise.setPrimaryMuscles(Set.of(muscle));
        exercise.setSecondaryMuscles(muscles);

        exerciseRepository.save(exercise);

        Exercise savedExercise = exerciseRepository.findAll().get(0);

        assertNotNull(savedExercise.getId());
        assertEquals(muscle, savedExercise.getPrimaryMuscle());
        assertEquals(muscles,  savedExercise.getSecondaryMuscles());
    }
}
