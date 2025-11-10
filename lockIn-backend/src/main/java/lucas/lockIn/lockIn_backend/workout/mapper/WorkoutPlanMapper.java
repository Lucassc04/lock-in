package lucas.lockIn.lockIn_backend.workout.mapper;

import lucas.lockIn.lockIn_backend.auth.mapper.UserMapper;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutPlanResponse;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SeriesMapper.class, UserMapper.class})
public interface WorkoutPlanMapper {

    WorkoutPlanResponse toResponse(WorkoutPlan workoutPlan);

    WorkoutPlan toEntity(WorkoutPlanResponse workoutPlanResponse);

    List<WorkoutPlanResponse> toResponseList(List<WorkoutPlan> workoutPlans);
}
