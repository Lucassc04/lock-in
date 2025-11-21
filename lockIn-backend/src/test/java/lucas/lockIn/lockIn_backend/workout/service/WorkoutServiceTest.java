package lucas.lockIn.lockIn_backend.workout.service;

import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.mapper.UserMapper;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.CurrentWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.request.*;
import lucas.lockIn.lockIn_backend.workout.dto.response.*;
import lucas.lockIn.lockIn_backend.workout.entity.*;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.mapper.*;
import lucas.lockIn.lockIn_backend.workout.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private ExecutedWorkoutRepository executedWorkoutRepository;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private UserService userService;

    @Spy
    private ExecutedWorkoutMapper executedWorkoutMapper = Mappers.getMapper(ExecutedWorkoutMapper.class);

    @Spy
    private SeriesMapper seriesMapper = Mappers.getMapper(SeriesMapper.class);

    @Spy
    private ExerciseMapper exerciseMapper = Mappers.getMapper(ExerciseMapper.class);

    @Spy
    private WorkoutMapper workoutMapper = Mappers.getMapper(WorkoutMapper.class);

    @Spy
    private WorkoutPlanMapper workoutPlanMapper = Mappers.getMapper(WorkoutPlanMapper.class);

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private WorkoutService workoutService;

    private Workout validWorkout;
    private Exercise validExercise;
    private WorkoutPlan validPlan;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(workoutMapper, "executedWorkoutMapper", executedWorkoutMapper);
        ReflectionTestUtils.setField(executedWorkoutMapper, "seriesMapper", seriesMapper);
        ReflectionTestUtils.setField(seriesMapper, "exerciseMapper", exerciseMapper);
        ReflectionTestUtils.setField(workoutMapper, "workoutPlanMapper", workoutPlanMapper);
        ReflectionTestUtils.setField(workoutPlanMapper, "seriesMapper", seriesMapper);
        ReflectionTestUtils.setField(workoutPlanMapper, "userMapper", userMapper);

        mockUser = User.builder()
                .id(0L)
                .email("email@email.com")
                .build();
        startTime = LocalDateTime.now().minusHours(1);
        finishTime = LocalDateTime.now();

        validExercise = Exercise.builder()
                .id(1L)
                .name("Bench Press")
                .description("Chest exercise")
                .build();

        validPlan = WorkoutPlan.builder()
                .id(1L)
                .name("Push Day")
                .build();

        ExecutedWorkout executedWorkout = new ExecutedWorkout();
        executedWorkout.setId(1L);

        validWorkout = new Workout();
        validWorkout.setId(1L);
        validWorkout.setUser(mockUser);
        validWorkout.setWorkoutPlan(validPlan);
        validWorkout.setExecutedWorkout(executedWorkout);
        validWorkout.setStartTime(startTime);
        validWorkout.setFinishTime(finishTime);
        validWorkout.setDuration(Duration.between(startTime, finishTime));
        validWorkout.setNotes("Nice workout");
    }

    @Test
    @DisplayName("Should find workout by id")
    void shouldFindWorkoutById() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(validWorkout));

        WorkoutResponse response = workoutService.findById(1L);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("Should throw exception when workout not found")
    void shouldThrowWhenWorkoutNotFound() {
        when(workoutRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Workout");
    }

    @Test
    @DisplayName("Should return all workouts")
    void shouldReturnAllWorkouts() {
        when(workoutRepository.findAll()).thenReturn(List.of(validWorkout));

        List<WorkoutResponse> responses = workoutService.findAll();

        assertThat(responses)
                .isNotEmpty()
                .hasSize(1)
                .extracting("id")
                .containsExactly(1L);
    }

    @Test
    @DisplayName("Should return user workout")
    void shouldReturnUserWorkout() {
        when(workoutRepository.findByIdAndUserId(validWorkout.getId(), mockUser.getId()))
                .thenReturn(Optional.of(validWorkout));

        WorkoutResponse response = workoutService.findByIdForUser(validWorkout.getId(), mockUser.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("Should return list of user workout")
    void shouldReturnListOfUserWorkout() {
        when(workoutRepository.findAllByUserId(mockUser.getId())).thenReturn(List.of(validWorkout));

        List<WorkoutResponse> response = workoutService.findAllForUser(mockUser.getId());

        assertThat(response)
                .isNotEmpty()
                .hasSize(1)
                .extracting("id")
                .containsExactly(1L);
    }

    @Test
    @DisplayName("Should create workout with plan")
    void shouldPostWorkoutWithPlan() {
        WorkingSeriesRequest workingSeriesRequest = new WorkingSeriesRequest(validExercise.getName(), 50.0, 10, 3);
        ExecutedWorkoutRequest executedRequest = new ExecutedWorkoutRequest(List.of(workingSeriesRequest), null);
        WorkoutRequest workoutRequest = new WorkoutRequest(executedRequest, startTime, finishTime, "Nice workout");

        when(workoutPlanRepository.findByIdAndUsers_Id(validPlan.getId(), mockUser.getId())).thenReturn(Optional.of(validPlan));
        when(exerciseService.findByName(any())).thenReturn(validExercise);
        when(executedWorkoutRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(workoutRepository.save(any())).thenReturn(validWorkout);

        WorkoutResponse response = workoutService.postWorkout(workoutRequest, 1L, mockUser.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("Should create workout without plan (null planId)")
    void shouldPostWorkoutWithoutPlan() {
        WorkingSeriesRequest workingSeriesRequest = new WorkingSeriesRequest(validExercise.getName(), 40.0, 12, 3);
        ExecutedWorkoutRequest executedRequest = new ExecutedWorkoutRequest(List.of(workingSeriesRequest), null);
        WorkoutRequest workoutRequest = new WorkoutRequest(executedRequest, startTime, finishTime, null);

        when(exerciseService.findByName(any())).thenReturn(validExercise);
        when(executedWorkoutRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(workoutRepository.save(any())).thenReturn(validWorkout);

        WorkoutResponse response = workoutService.postWorkout(workoutRequest, null,  mockUser.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("Should start workout without plan")
    void shouldStartWorkoutWithoutPlan() {
        when(workoutRepository.save(any())).thenAnswer(invocation -> {
            Workout w = invocation.getArgument(0);
            w.setId(1L);
            return w;
        });
        when(workoutRepository.findOngoingWorkout(any())).thenReturn(Optional.empty());
        when(userService.findById(any())).thenReturn(mockUser);

        CurrentWorkoutDTO dto = workoutService.startWorkout(null, mockUser.getId());

        assertThat(dto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("Should start workout with plan")
    void shouldStartWorkoutWithPlan() {
        when(workoutPlanRepository.findByIdAndUsers_Id(any(), any())).thenReturn(Optional.of(validPlan));
        when(workoutRepository.save(any())).thenAnswer(invocation -> {
            Workout w = invocation.getArgument(0);
            w.setId(1L);
            return w;
        });
        when(userService.findById(any())).thenReturn(mockUser);

        CurrentWorkoutDTO dto = workoutService.startWorkout(validPlan.getId(), mockUser.getId());

        assertThat(dto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("Should finish workout successfully")
    void shouldFinishWorkout() {
        WorkingSeriesRequest workingSeriesRequest = new WorkingSeriesRequest(validExercise.getName(), 45.0, 8, 3);
        WarmupSeriesRequest warmupSeriesRequest = new WarmupSeriesRequest(validExercise.getName(), 20.0, 10, 1);
        ExecutedWorkoutRequest executedRequest = new ExecutedWorkoutRequest(List.of(workingSeriesRequest), List.of(warmupSeriesRequest));

        when(workoutRepository.findOngoingWorkout(any())).thenReturn(Optional.of(validWorkout));
        when(exerciseService.findByName(any())).thenReturn(validExercise);
        when(executedWorkoutRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(workoutRepository.save(any())).thenReturn(validWorkout);

        WorkoutResponse response = workoutService.finishWorkout(mockUser.getId(), executedRequest);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("Should throw when finishing non-existent workout")
    void shouldThrowWhenFinishingNonexistentWorkout() {
        when(workoutRepository.findOngoingWorkout(any())).thenReturn(Optional.empty());

        ExecutedWorkoutRequest executedRequest = new ExecutedWorkoutRequest(List.of(), null);

        assertThatThrownBy(() -> workoutService.finishWorkout(99L, executedRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("workout");
    }
}
