package lucas.lockIn.lockIn_backend.workout.service;

import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.mapper.UserMapper;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.request.PlannedSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutPlanRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutPlanResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.PlannedSeries;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.mapper.*;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WorkoutPlanServiceTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private ExerciseService exerciseService;

    @Spy
    private SeriesMapper seriesMapper = Mappers.getMapper(SeriesMapper.class);

    @Spy
    private ExerciseMapper exerciseMapper = Mappers.getMapper(ExerciseMapper.class);

    @Spy
    private WorkoutPlanMapper workoutPlanMapper = Mappers.getMapper(WorkoutPlanMapper.class);

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserService userService;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;


    private WorkoutPlan validPlan;
    private Exercise validExercise;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        mockUser = User.builder()
                        .id(0L)
                        .build();

        ReflectionTestUtils.setField(workoutPlanMapper, "seriesMapper", seriesMapper);
        ReflectionTestUtils.setField(seriesMapper, "exerciseMapper", exerciseMapper);
        ReflectionTestUtils.setField(workoutPlanMapper, "userMapper", userMapper);

        validExercise = Exercise.builder()
                .id(1L)
                .name("Bench Press")
                .description("Chest exercise")
                .build();

        PlannedSeries plannedSeries = new PlannedSeries(validExercise,3);

        validPlan = WorkoutPlan.builder()
                .id(1L)
                .name("Push Day")
                .series(List.of(plannedSeries))
                .creator(mockUser)
                .users(new HashSet<>(List.of(mockUser)))
                .build();
    }

    @Test
    @DisplayName("Should find workout plan by id")
    void shouldFindWorkoutPlanById() {
        // Arrange
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(validPlan));

        // Act
        WorkoutPlanResponse response = workoutPlanService.findById(1L);

        // Assert
        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Push Day");
    }

    @Test
    @DisplayName("Should throw exception when workout plan is not found")
    void shouldThrowWhenWorkoutPlanNotFound() {
        // Arrange
        when(workoutPlanRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> workoutPlanService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Workout Plan");
    }

    @Test
    @DisplayName("Should return all workout plans")
    void shouldReturnAllWorkoutPlans() {
        // Arrange
        when(workoutPlanRepository.findAll()).thenReturn(List.of(validPlan));

        // Act
        List<WorkoutPlanResponse> responseList = workoutPlanService.findAll();

        // Assert
        assertThat(responseList)
                .isNotEmpty()
                .hasSize(1)
                .extracting("name")
                .containsExactly("Push Day");
    }

    @Test
    @DisplayName("Should create a workout plan with series")
    void shouldCreateWorkoutPlan() {
        // Arrange
        PlannedSeriesRequest plannedSeriesRequest = new PlannedSeriesRequest("Bench Press", 3);
        WorkoutPlanRequest request = new WorkoutPlanRequest("Push Day", List.of(plannedSeriesRequest));


        when(exerciseService.findByName("Bench Press")).thenReturn(validExercise);
        when(workoutPlanRepository.save(any())).thenReturn(validPlan);
        when(userService.findById(any())).thenReturn(mockUser);

        // Act
        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(request, mockUser.getId());

        // Assert
        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Push Day");
    }

    @Test
    @DisplayName("Should update a workout plan")
    void shouldUpdateWorkoutPlan() {
        // Arrange
        PlannedSeriesRequest seriesRequest = new PlannedSeriesRequest("Bench Press", 4);
        WorkoutPlanRequest request = new WorkoutPlanRequest("Updated Plan", List.of(seriesRequest));
        WorkoutPlan updatedPlan = WorkoutPlan.builder()
                .id(1L)
                .name("Updated Plan")
                .series(List.of(new PlannedSeries(validExercise,4)))
                .build();

        when(workoutPlanRepository.findByIdAndUsers_Id(validPlan.getId(), mockUser.getId())).thenReturn(Optional.of(validPlan));
        when(workoutPlanRepository.save(any())).thenReturn(updatedPlan);
        when(exerciseService.findByName("Bench Press")).thenReturn(validExercise);

        // Act
        WorkoutPlanResponse response = workoutPlanService.updateWorkoutPlan(1L, request, mockUser.getId());

        // Assert
        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Updated Plan");
    }

    @Test
    @DisplayName("Should delete workout plan by id")
    void shouldDeleteWorkoutPlan() {
        // Arrange
        doNothing().when(workoutPlanRepository).delete(any());
        when(workoutPlanRepository.findByIdAndUsers_Id(validPlan.getId(), mockUser.getId())).thenReturn(Optional.of(validPlan));

        // Act
        workoutPlanService.deleteWorkoutPlan(1L, mockUser.getId());

        // Assert
        verify(workoutPlanRepository, times(1)).delete(any());
    }
}
