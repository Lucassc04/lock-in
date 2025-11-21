package lucas.lockIn.lockIn_backend.workout.mapper;

import lucas.lockIn.lockIn_backend.workout.dto.response.PlannedSeriesResponse;
import lucas.lockIn.lockIn_backend.workout.dto.response.WarmupSeriesResponse;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkingSeriesResponse;
import lucas.lockIn.lockIn_backend.workout.entity.PlannedSeries;
import lucas.lockIn.lockIn_backend.workout.entity.WarmupSeries;
import lucas.lockIn.lockIn_backend.workout.entity.WorkingSeries;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ExerciseMapper.class})
public interface SeriesMapper {

    //For all the classes, need to explicitly map the attributes that the classes
    // inherit from the Series class
    @Mapping(source = "exercise", target = "exercise")
    @Mapping(source = "series", target = "series")
    PlannedSeriesResponse toResponse(PlannedSeries plannedSeries);

    @Mapping(source = "exercise", target = "exercise")
    @Mapping(source = "series", target = "series")
    @Mapping(target = "id", ignore = true)
    PlannedSeries toEntity(PlannedSeriesResponse plannedSeriesResponse);

    @Mapping(source = "exercise", target = "exercise")
    @Mapping(source = "series", target = "series")
    WorkingSeriesResponse toResponse(WorkingSeries workingSeries);

    @Mapping(source = "exercise", target = "exercise")
    @Mapping(source = "series", target = "series")
    @Mapping(target = "id", ignore = true)
    WorkingSeries toEntity(WorkingSeriesResponse workingSeriesResponse);

    @Mapping(source = "exercise", target = "exercise")
    @Mapping(source = "series", target = "series")
    WarmupSeriesResponse toResponse(WarmupSeries warmupSeries);

    @Mapping(source = "exercise", target = "exercise")
    @Mapping(source = "series", target = "series")
    @Mapping(target = "id", ignore = true)
    WarmupSeries toEntity(WarmupSeriesResponse warmupSeriesResponse);
}