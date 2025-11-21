package lucas.lockIn.lockIn_backend.workout.mapper;

import lucas.lockIn.lockIn_backend.workout.dto.CurrentWorkoutDTO;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ExecutedWorkoutMapper.class, WorkoutPlanMapper.class})
public interface WorkoutMapper {

    WorkoutResponse toResponse(Workout workout);

    @Mapping(target = "workoutPlan", ignore = true)
    @Mapping(target = "user", ignore = true)
    Workout toEntity(WorkoutResponse response);

    CurrentWorkoutDTO toCurrent(Workout workout);

    List<WorkoutResponse> toResponseList(List<Workout> workouts);
}
