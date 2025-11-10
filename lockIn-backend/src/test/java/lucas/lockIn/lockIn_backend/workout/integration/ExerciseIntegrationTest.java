package lucas.lockIn.lockIn_backend.workout.integration;

import lucas.lockIn.lockIn_backend.auth.dto.request.RegisterRequest;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.service.ExerciseService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ExerciseIntegrationTest {

    @Autowired private ExerciseService exerciseService;
    @Autowired private UserService userService;

    private UserPrincipal userPrincipal;
    private ExerciseRequest exerciseRequest;

    @BeforeAll
    void setUp() {
        RegisterRequest registerRequest = new RegisterRequest(
                "No", "Body", "nobody@gmail.com", "justamockpassword");
        userPrincipal = userService.createUser(registerRequest);

        exerciseRequest = new ExerciseRequest(
                "Bench Press",
                new HashSet<>(List.of(Muscle.MIDDLE_CHEST)),
                new HashSet<>(List.of(Muscle.SHORT_HEAD_TRICEPS, Muscle.FRONT_DELTS)),
                "Use a barbell");
    }

    @Test
    @DisplayName("Should Create Exercise and Get Exercise")
    void shouldCreateExerciseAndGetExercise() {
        //Act
        ExerciseResponse response = exerciseService.createExercise(exerciseRequest, userPrincipal.getUserId());
        ExerciseResponse getResponse = exerciseService.findByIdForUser(response.id(), userPrincipal.getUserId());
        //Assert
        assertThat(response)
                .isEqualTo(getResponse)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name",exerciseRequest.name())
                .hasFieldOrPropertyWithValue("description",exerciseRequest.description())
                .hasFieldOrPropertyWithValue("primaryMuscles", exerciseRequest.primaryMuscles())
                .hasFieldOrPropertyWithValue("secondaryMuscles", exerciseRequest.secondaryMuscles());

    }

    @Test
    @DisplayName("Should Update Exercise")
    void  shouldUpdateExerciseAndGetExercise() {
        ExerciseRequest updated = new ExerciseRequest(
                "Dips",
                new HashSet<>(List.of(Muscle.LOWER_CHEST, Muscle.SHORT_HEAD_TRICEPS)),
                new HashSet<>(List.of(Muscle.FRONT_DELTS, Muscle.SHORT_HEAD_TRICEPS)), null);

        ExerciseResponse e = exerciseService.createExercise(exerciseRequest, userPrincipal.getUserId());
        ExerciseResponse response = exerciseService.updateExercise(e.id(), updated, userPrincipal.getUserId());
        ExerciseResponse getResponse = exerciseService.findByIdForUser(response.id(), userPrincipal.getUserId());

        assertThat(response)
                .isEqualTo(getResponse)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name",updated.name())
                .hasFieldOrPropertyWithValue("description",updated.description())
                .hasFieldOrPropertyWithValue("primaryMuscles", updated.primaryMuscles())
                .hasFieldOrPropertyWithValue("secondaryMuscles", new HashSet<>(List.of(Muscle.FRONT_DELTS)));
    }

    @Test
    @DisplayName("Should Delete Exercise")
    void shouldDeleteExerciseAndGetExercise() {

        ExerciseResponse e = exerciseService.createExercise(exerciseRequest, userPrincipal.getUserId());
        exerciseService.deleteExercise(e.id(), userPrincipal.getUserId());

        assertThatThrownBy(() ->  exerciseService.findByIdForUser(e.id(), userPrincipal.getUserId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Exercise");
    }
}
