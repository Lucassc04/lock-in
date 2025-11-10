package lucas.lockIn.lockIn_backend.workout.service;

import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import lucas.lockIn.lockIn_backend.workout.entity.Muscle;
import lucas.lockIn.lockIn_backend.workout.mapper.ExerciseMapperImpl;
import lucas.lockIn.lockIn_backend.workout.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Spy
    private ExerciseMapperImpl exerciseMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise validExercise;

    @BeforeEach
    public void setUp() {
        User mockUser = User.builder().id(0L).build();

        Set<Muscle> primaryMuscles = new HashSet<>(List.of(Muscle.MIDDLE_CHEST));
        Set<Muscle> secondaryMuscles
                = new HashSet<>(Arrays.asList(Muscle.SHORT_HEAD_TRICEPS, Muscle.MEDIAL_HEAD_TRICEPS));

        validExercise = Exercise.builder()
                .name("Bench Press")
                .primaryMuscles(primaryMuscles)
                .secondaryMuscles(secondaryMuscles)
                .description("With 20kg bar")
                .id(1L)
                .creator(mockUser)
                .build();
    }

    @Test
    @DisplayName("Should create a normal exercise")
    void shouldCreateExercise(){
        //Arrange
        ExerciseRequest request = new ExerciseRequest(
                "Bench Press", validExercise.getPrimaryMuscles(), validExercise.getSecondaryMuscles(), "With 20kg bar");

        when(exerciseRepository.save(any())).thenReturn(validExercise);

        //Act
        ExerciseResponse response = exerciseService.createExercise(request, 0L);

        //Assert
        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", request.name())
                .hasFieldOrPropertyWithValue("primaryMuscles", request.primaryMuscles())
                .hasFieldOrPropertyWithValue("secondaryMuscles", request.secondaryMuscles())
                .hasFieldOrPropertyWithValue("description", request.description());
    }

    @Test
    @DisplayName("Should create an exercise without secondary muscles (pruned), because they are the same as" +
            "the primary muscles.")
    void shouldCreateExerciseWithPrunedSecondaryMuscles(){
        //Arrange
        validExercise.setSecondaryMuscles(new HashSet<>(List.of()));
        ExerciseRequest request = new ExerciseRequest(
                "Bench Press", validExercise.getPrimaryMuscles(), validExercise.getPrimaryMuscles(),null);

        when(exerciseRepository.save(any())).thenReturn(validExercise);


        //Act
        ExerciseResponse response = exerciseService.createExercise(request, 0L);

        //Assert
        assertThat(response)
                .hasFieldOrPropertyWithValue("secondaryMuscles", new HashSet<>(List.of()));

    }

    @Test
    @DisplayName("Should update the exercise")
    void shouldUpdateExercise() {
        //Arrange
        ExerciseRequest request = new ExerciseRequest(
                "Bench Press", validExercise.getPrimaryMuscles(), new HashSet<>(List.of(Muscle.MIDDLE_CHEST)),null);

        when(exerciseRepository.save(any())).thenReturn(validExercise);
        exerciseService.createExercise(request, 0L);

        //After the exercise was created, now update it
        User user = User.builder()
                .id(0L).build();

        Exercise exercise = Exercise.builder()
                .name("Dips")
                .primaryMuscles(new HashSet<>(Arrays.asList(Muscle.MIDDLE_CHEST, Muscle.SHORT_HEAD_TRICEPS, Muscle.MEDIAL_HEAD_TRICEPS)))
                .id(1L)
                .creator(user)
                .build();

        ExerciseRequest request2 = new ExerciseRequest(exercise.getName(),
                exercise.getPrimaryMuscles(), exercise.getSecondaryMuscles(),exercise.getDescription());

        when(exerciseRepository.save(any())).thenReturn(exercise);
        when(exerciseRepository.findByIdAndUserId(validExercise.getId(), user.getId())).thenReturn(Optional.of(validExercise));

        //Act
        ExerciseResponse response = exerciseService.updateExercise(validExercise.getId(), request2, user.getId());

        //Assert
        assertThat(response)
                .hasFieldOrPropertyWithValue("primaryMuscles", new HashSet<>(List.of(Muscle.SHORT_HEAD_TRICEPS, Muscle.MIDDLE_CHEST, Muscle.MEDIAL_HEAD_TRICEPS)))
                .hasFieldOrPropertyWithValue("name", "Dips");
    }




}