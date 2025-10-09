package lucas.lockIn.lockIn_backend.workout.mapper;

import lucas.lockIn.lockIn_backend.workout.dto.response.ExecutedWorkoutPlanResponse;
import lucas.lockIn.lockIn_backend.workout.entity.ExecutedWorkoutPlan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SeriesMapper.class})
public interface ExecutedWorkoutPlanMapper {

    ExecutedWorkoutPlanResponse toResponse(ExecutedWorkoutPlan plan);

    ExecutedWorkoutPlan toEntity(ExecutedWorkoutPlanResponse planResponse);

}
