package lucas.lockIn.lockIn_backend.workout.service;

import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import lucas.lockIn.lockIn_backend.workout.dto.request.WorkoutPlanRequest;
import lucas.lockIn.lockIn_backend.workout.dto.request.PlannedSeriesRequest;
import lucas.lockIn.lockIn_backend.workout.dto.response.WorkoutPlanResponse;
import lucas.lockIn.lockIn_backend.workout.entity.PlannedSeries;
import lucas.lockIn.lockIn_backend.workout.entity.WorkoutPlan;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.exceptions.OwnershipError;
import lucas.lockIn.lockIn_backend.workout.mapper.WorkoutPlanMapperImpl;
import lucas.lockIn.lockIn_backend.workout.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseService exerciseService;
    private final UserService userService;
    private final WorkoutPlanMapperImpl mapper;


    public WorkoutPlanResponse findById(Long id) {
        return mapper.toResponse(workoutPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout Plan", id)));
    }

    public List<WorkoutPlanResponse> findAll() {
        return mapper.toResponseList(workoutPlanRepository.findAll());
    }

    public WorkoutPlanResponse findByIdForUser(Long workoutPlanId, Long userId) {
        return mapper.toResponse(workoutPlanRepository.findByIdAndUsers_Id(workoutPlanId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Workout Plan", workoutPlanId)));
    }

    public List<WorkoutPlanResponse> findAllForUser(Long userId) {
        return mapper.toResponseList(workoutPlanRepository.findAllByUserId(userId));
    }

    public WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest workoutPlanRequest, Long userId) {
        WorkoutPlan workoutPlan = new WorkoutPlan();

        User user =  userService.findById(userId);
        workoutPlan.setCreator(user);
        workoutPlan.addUsers(user);

        workoutPlan.setName(workoutPlanRequest.name());
        workoutPlan.setSeries(convertSeriesRequestToEntities(workoutPlanRequest.series()));

        workoutPlan = workoutPlanRepository.save(workoutPlan);
        return mapper.toResponse(workoutPlan);
    }


    public WorkoutPlanResponse updateWorkoutPlan(Long workoutPlanId, WorkoutPlanRequest workoutPlanRequest, Long userId) {
        WorkoutPlan workoutPlan = mapper.toEntity(findByIdForUser(workoutPlanId, userId));

        if(!userIsOwner(workoutPlan, userId)) {
            throw new OwnershipError("Workout Plan doesn't belong to this user");
        }

        workoutPlan.setName(workoutPlanRequest.name());
        workoutPlan.setSeries(convertSeriesRequestToEntities(workoutPlanRequest.series()));

        workoutPlan = workoutPlanRepository.save(workoutPlan);
        return mapper.toResponse(workoutPlan);
    }

    public void subscribeToWorkoutPlan(Long workoutPlanId, Long userId) {
        WorkoutPlan workoutPlan = mapper.toEntity(findById(workoutPlanId));
        User user =  userService.findById(userId);

        if(workoutPlan.getUsers().contains(user)) {
            return;
        }
        workoutPlan.addUsers(user);
        workoutPlanRepository.save(workoutPlan);
    }

    public void deleteWorkoutPlan(Long workoutPlanId, Long userId) {
        WorkoutPlan workoutPlan = mapper.toEntity(findByIdForUser(workoutPlanId, userId));

        if(userIsOwner(workoutPlan, userId)) {
            workoutPlanRepository.delete(workoutPlan);
            return;
        }
        throw new OwnershipError("Workout Plan doesn't belong to this user");
    }

    private boolean userIsOwner(WorkoutPlan workoutPlan, Long userId) {
        return workoutPlan.getCreator().getId().equals(userId);
    }

    private List<PlannedSeries> convertSeriesRequestToEntities(List<PlannedSeriesRequest> series) {
        return series.stream().
                map(this::fromRequest).
                collect(Collectors.toList());
    }

    private PlannedSeries fromRequest(PlannedSeriesRequest request) {
        return new PlannedSeries(exerciseService.findByName(request.exerciseName()), request.series());
    }
}
