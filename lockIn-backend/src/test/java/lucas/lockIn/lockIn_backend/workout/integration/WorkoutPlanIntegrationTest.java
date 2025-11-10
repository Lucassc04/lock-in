package lucas.lockIn.lockIn_backend.workout.integration;

import lucas.lockIn.lockIn_backend.auth.dto.request.RegisterRequest;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.PlannedSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutPlanRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutPlanResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.service.ExerciseService;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutPlanService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class WorkoutPlanIntegrationTest {

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private UserService userService;


    private List<PlannedSeriesRequest>  plannedSeriesRequests = new ArrayList<>();
    private UserPrincipal userPrincipal;

    @BeforeAll
    void setup(){
        userPrincipal = userService.createUser(new RegisterRequest(
                "No", "Body", "nobody@gmail.com", "justapassword"));
        ExerciseRequest e1 = new ExerciseRequest(
                "Bench Press",
                new HashSet<>(List.of(Muscle.MIDDLE_CHEST)),
                new HashSet<>(List.of(Muscle.FRONT_DELTS, Muscle.SHORT_HEAD_TRICEPS, Muscle.MEDIAL_HEAD_TRICEPS)),
                null);
        ExerciseRequest e2 = new ExerciseRequest(
                "Triceps Extension",
                new HashSet<>(List.of(Muscle.LONG_HEAD_TRICEPS)),
                null,
                "With ropes");
        ExerciseRequest e3 = new ExerciseRequest(
                "Shoulder Press",
                new HashSet<>(List.of(Muscle.FRONT_DELTS, Muscle.LATERAL_DELTS)),
                null,
                "With Dumbbells");

        exerciseService.createExercise(e1, userPrincipal.getUserId());
        exerciseService.createExercise(e2, userPrincipal.getUserId());
        exerciseService.createExercise(e3, userPrincipal.getUserId());

        plannedSeriesRequests.add(new PlannedSeriesRequest(e1.name(), 3));
        plannedSeriesRequests.add(new PlannedSeriesRequest(e2.name(), 2));
        plannedSeriesRequests.add(new PlannedSeriesRequest(e3.name(), 4));
    }

    @Test
    @DisplayName("Create Workout Plan")
    void shouldCreateWorkoutPlan() {
        WorkoutPlanRequest workoutPlanRequest = new WorkoutPlanRequest("Push A", plannedSeriesRequests);

        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(workoutPlanRequest, userPrincipal.getUserId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", workoutPlanRequest.name());
        assertThat(response.creator())
                .hasFieldOrPropertyWithValue("id", userPrincipal.getUserId());
        assertThat(response.series())
                .extracting("exercise.name")
                .containsExactlyInAnyOrder("Bench Press", "Triceps Extension", "Shoulder Press");
    }

    @Test
    @DisplayName("Update Workout Plan")
    void shouldUpdateWorkoutPlan() {
        //Arrange
        WorkoutPlanRequest workoutPlanRequest = new WorkoutPlanRequest("Push A", plannedSeriesRequests);
        ExerciseRequest e4 = new ExerciseRequest(
                "Dips",
                new HashSet<>(List.of(Muscle.LOWER_CHEST, Muscle.SHORT_HEAD_TRICEPS,  Muscle.MEDIAL_HEAD_TRICEPS)),
                null,
                "Bodyweight Only");
        exerciseService.createExercise(e4, userPrincipal.getUserId());
        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(workoutPlanRequest, userPrincipal.getUserId());
        List<PlannedSeriesRequest> newPlannedSeriesRequests = new ArrayList<>(plannedSeriesRequests);

        newPlannedSeriesRequests.add(new PlannedSeriesRequest(e4.name(), 1));
        WorkoutPlanRequest updateRequest = new WorkoutPlanRequest("Push A Updated", newPlannedSeriesRequests);

        //Act
        response = workoutPlanService.updateWorkoutPlan(response.id(), updateRequest, userPrincipal.getUserId());

        //Assert
        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", updateRequest.name());
        assertThat(response.creator())
                .hasFieldOrPropertyWithValue("id", userPrincipal.getUserId());
        assertThat(response.series())
                .extracting("exercise.name")
                .containsExactlyInAnyOrder("Bench Press", "Triceps Extension", "Shoulder Press", "Dips");
    }

    @Test
    @DisplayName("Delete Workout Plan")
    void shouldDeleteWorkoutPlan() {
        WorkoutPlanRequest workoutPlanRequest = new WorkoutPlanRequest("Push A", plannedSeriesRequests);
        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(workoutPlanRequest, userPrincipal.getUserId());

        workoutPlanService.deleteWorkoutPlan(response.id(), userPrincipal.getUserId());
        List<WorkoutPlanResponse> wpList = workoutPlanService.findAllForUser(userPrincipal.getUserId());

        assertThat(wpList)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    @DisplayName("Subscribe to Workout Plan")
    void shouldSubscribeToWorkoutPlan() {
        UserPrincipal newUser = userService.createUser(new RegisterRequest(
                "Some", "Body", "somebody@gmail.com", "justapassword"));
        WorkoutPlanRequest workoutPlanRequest = new WorkoutPlanRequest("Push A", plannedSeriesRequests);
        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(workoutPlanRequest, newUser.getUserId());

        workoutPlanService.subscribeToWorkoutPlan(response.id(), userPrincipal.getUserId());
        List<WorkoutPlanResponse> wpList = workoutPlanService.findAllForUser(userPrincipal.getUserId());

        assertThat(wpList)
                .isNotNull()
                .hasSize(1);
    }
}
