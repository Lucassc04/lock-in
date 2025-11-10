package lucas.lockIn.lockIn_backend.workout.mapper;

import lucas.lockIn.lockIn_backend.workout.dto.request.ExerciseRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExecutedExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.dto.response.ExerciseResponse;
import lucas.lockIn.lockIn_backend.workout.entity.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ExerciseMapper {

    Exercise toEntity(ExerciseResponse dto);

    ExerciseResponse toResponseDto(Exercise entity);

    ExecutedExerciseResponse toExecutedExerciseResponse(Exercise entity);

    List<ExerciseResponse> toResponseDto(List<Exercise> entities);

    List<Exercise> toEntity(List<ExerciseResponse> dtos);
}
