package lucas.lockIn.lockIn_backend.workout.integration;

import lucas.lockIn.lockIn_backend.auth.dto.request.RegisterRequest;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.CurrentWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.request.*;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutPlanResponse;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.service.ExerciseService;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutPlanService;
import lucas.lockIn.lockIn_backend.workout.service.WorkoutService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class WorkoutIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private WorkoutService workoutService;
    @Autowired
    private WorkoutPlanService workoutPlanService;
    @Autowired
    private ExerciseService exerciseService;

    private UserPrincipal userPrincipal;
    private WorkoutPlanResponse workoutPlan;
    private static ExerciseRequest e1;
    private static ExerciseRequest e2;
    private static ExerciseRequest e3;

    @BeforeEach
    void setUp() {
        RegisterRequest registerRequest = new RegisterRequest(
                "No", "Body", "nobody@gmail.com", "justamockpassword");
        userPrincipal = userService.createUser(registerRequest);

        e1 = new ExerciseRequest(
                "Bench Press",
                new HashSet<>(List.of(Muscle.MIDDLE_CHEST)),
                new HashSet<>(List.of(Muscle.FRONT_DELTS, Muscle.SHORT_HEAD_TRICEPS, Muscle.MEDIAL_HEAD_TRICEPS)),
                null);
        e2 = new ExerciseRequest(
                "Triceps Extension",
                new HashSet<>(List.of(Muscle.LONG_HEAD_TRICEPS)),
                null,
                "With ropes");
        e3 = new ExerciseRequest(
                "Shoulder Press",
                new HashSet<>(List.of(Muscle.FRONT_DELTS, Muscle.LATERAL_DELTS)),
                null,
                "With Dumbbells");

        exerciseService.createExercise(e1, userPrincipal.getUserId());
        exerciseService.createExercise(e2, userPrincipal.getUserId());
        exerciseService.createExercise(e3, userPrincipal.getUserId());
        List<PlannedSeriesRequest> plannedSeriesRequests = new ArrayList<>();
        plannedSeriesRequests.add(new PlannedSeriesRequest(e1.name(), 3));
        plannedSeriesRequests.add(new PlannedSeriesRequest(e2.name(), 2));
        plannedSeriesRequests.add(new PlannedSeriesRequest(e3.name(), 4));
        WorkoutPlanRequest request =  new WorkoutPlanRequest("Push A", plannedSeriesRequests);
        workoutPlan = workoutPlanService.createWorkoutPlan(request, userPrincipal.getUserId());
    }

    @Test
    void shouldStartWorkoutWithPlan() {
        CurrentWorkoutDTO workout = workoutService.startWorkout(workoutPlan.id(),  userPrincipal.getUserId());

        assertThat(workout)
                .isNotNull()
                .hasFieldOrPropertyWithValue("workoutPlan",  workoutPlan);
        assertThat(workout.startTime())
                .isNotNull()
                .isBefore(LocalDateTime.now().plusMinutes(10));
    }

    @Test
    void shouldStartWorkoutWithoutPlan() {
        CurrentWorkoutDTO workout = workoutService.startWorkout(null, userPrincipal.getUserId());

        assertThat(workout)
                .isNotNull()
                .hasFieldOrPropertyWithValue("workoutPlan", null);
        assertThat(workout.startTime())
                .isNotNull()
                .isBefore(LocalDateTime.now().plusMinutes(10));
    }

    @Test
    void shouldStartAndCancelWorkout() {
        workoutService.startWorkout(workoutPlan.id(), userPrincipal.getUserId());

        workoutService.cancelOngoingWorkout(userPrincipal.getUserId());
        List<WorkoutResponse> wkList = workoutService.findAllForUser(userPrincipal.getUserId());

        assertThat(wkList)
                .hasSize(0);
    }
    
    @Test
    void shouldStartAndFinishWorkout() {
        workoutService.startWorkout(workoutPlan.id(), userPrincipal.getUserId());
        ExecutedWorkoutRequest executedWorkoutRequest = getExecutedWorkoutPlanRequest();

        WorkoutResponse workoutResponse = workoutService.finishWorkout(userPrincipal.getUserId(), executedWorkoutRequest);

        assertThat(workoutResponse.executedWorkout().workingSeries())
                .isNotNull()
                .hasSize(4);
        assertThat(workoutResponse.executedWorkout().warmupSeries())
                .isNotNull()
                .hasSize(2);
        assertThat(workoutResponse.finishTime())
                .isAfter(workoutResponse.startTime());
    }

    @Test
    void shouldPostWorkout() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime finish = LocalDateTime.now();
        ExecutedWorkoutRequest executedWorkoutRequest = getExecutedWorkoutPlanRequest();
        WorkoutRequest workoutRequest = new WorkoutRequest(
                executedWorkoutRequest, start, finish, "Was tough");

        WorkoutResponse response = workoutService.postWorkout(workoutRequest, null, userPrincipal.getUserId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("startTime", start)
                .hasFieldOrPropertyWithValue("finishTime", finish)
                .hasFieldOrPropertyWithValue("notes", workoutRequest.notes());
        assertThat(response.executedWorkout())
                .isNotNull();
        Assertions.assertNotNull(executedWorkoutRequest.warmupSeries());
        assertThat(response.executedWorkout().warmupSeries())
                .hasSize(executedWorkoutRequest.warmupSeries().size());
        assertThat(response.executedWorkout().workingSeries())
                .hasSize(executedWorkoutRequest.workingSeries().size());
    }

    private static ExecutedWorkoutRequest getExecutedWorkoutPlanRequest() {
        List<WorkingSeriesRequest> workingSeriesRequests = new ArrayList<>();
        List<WarmupSeriesRequest> warmupSeriesRequests = new ArrayList<>();
        workingSeriesRequests.add(new WorkingSeriesRequest(e1.name(), 20d, 5, 2));
        workingSeriesRequests.add(new WorkingSeriesRequest(e1.name(), 15d,7, 1));
        workingSeriesRequests.add(new WorkingSeriesRequest(e2.name(), 15d,7, 2));
        workingSeriesRequests.add(new WorkingSeriesRequest(e3.name(), 25d,4, 2));
        warmupSeriesRequests.add(new WarmupSeriesRequest(e1.name(), 10d,5, 2));
        warmupSeriesRequests.add(new WarmupSeriesRequest(e2.name(), 10d,5, 2));
        return new ExecutedWorkoutRequest(
                workingSeriesRequests, warmupSeriesRequests);
    }
}
