package lucas.lockIn.lockIn_backend.workout;

import lucas.lockIn.lockIn_backend.workout.dto.CreateAndUpdateExerciseDTO;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ExerciseServiceTest {

    @Autowired
    private ExerciseService exerciseService;

    private CreateAndUpdateExerciseDTO benchPress;
    private CreateAndUpdateExerciseDTO squat;
    private CreateAndUpdateExerciseDTO upperBackRow;
    private CreateAndUpdateExerciseDTO chinUps;


    @BeforeEach
    public void setup() {
        benchPress = new CreateAndUpdateExerciseDTO(
                "Bench Press",
                Set.of(Muscle.MIDDLE_CHEST),
                Set.of(Muscle.SHORT_HEAD_TRICEPS, Muscle.FRONT_DELTS),
                ""
        );

        squat = new CreateAndUpdateExerciseDTO(
                "Squat",
                Set.of(Muscle.QUADS),
                Set.of(Muscle.MAXIMUS_GLUTES),
                ""
        );

        upperBackRow = new CreateAndUpdateExerciseDTO(
                "Upper Back Row",
                Set.of(Muscle.RHOMBOIDS_UPPER_BACK, Muscle.TRAPEZIUS_UPPER_BACK, Muscle.TERES_MAJOR_UPPER_BACK),
                Set.of(Muscle.SHORT_HEAD_BICEPS, Muscle.REAR_DELTS, Muscle.LONG_HEAD_BICEPS),
                ""
        );

        chinUps = new CreateAndUpdateExerciseDTO(
                "Chin Ups",
                Set.of(Muscle.LATS, Muscle.SHORT_HEAD_BICEPS, Muscle.LONG_HEAD_BICEPS),
                Set.of(),
                ""
        );
    }

    @Test
    public void test_createExercise_and_Get(){
        Exercise exercise = exerciseService.createExercise(benchPress);
        Exercise exercise2 = exerciseService.createExercise(squat);

        assertNotEquals(exercise, exercise2);
        assertNotEquals(exercise.getId(), exercise2.getId());
        assertNotEquals(exercise.getName(), exercise2.getName());
        assertNotEquals(exercise.getPrimaryMuscle(), exercise2.getPrimaryMuscle());

        exerciseService.createExercise(chinUps);
        exerciseService.createExercise(upperBackRow);

        List<Exercise> exercises = exerciseService.findAll();
        assertEquals(4, exercises.size());
    }
}
