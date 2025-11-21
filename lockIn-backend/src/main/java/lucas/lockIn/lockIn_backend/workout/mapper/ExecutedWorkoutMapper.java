package lucas.lockIn.lockIn_backend.workout.mapper;

import lucas.lockIn.lockIn_backend.workout.dto.response.ExecutedWorkoutResponse;
import lucas.lockIn.lockIn_backend.workout.entity.ExecutedWorkout;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SeriesMapper.class})
public interface ExecutedWorkoutMapper {

    ExecutedWorkoutResponse toResponse(ExecutedWorkout plan);

    ExecutedWorkout toEntity(ExecutedWorkoutResponse planResponse);

}
