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
        return mapper.toResponse(workoutPlanRepository.findByIdAndUserId(workoutPlanId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Workout Plan", workoutPlanId)));
    }

    public List<WorkoutPlanResponse> findAllForUser(Long userId) {
        return mapper.toResponseList(workoutPlanRepository.findAllByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Workout Plan", userId)));
    }

    public WorkoutPlanResponse createWorkoutPlan(Long userId, WorkoutPlanRequest workoutPlanRequest) {
        WorkoutPlan workoutPlan = new WorkoutPlan();

        User user =  userService.findById(userId);
        workoutPlan.setCreator(user);
        workoutPlan.setUsers(new HashSet<>(List.of(user)));

        workoutPlan.setName(workoutPlanRequest.name());
        workoutPlan.setSeries(convertSeriesRequestToEntities(workoutPlanRequest.series()));

        workoutPlan = workoutPlanRepository.save(workoutPlan);
        return mapper.toResponse(workoutPlan);
    }


    public WorkoutPlanResponse updateWorkoutPlan(Long userId, Long workoutPlanId, WorkoutPlanRequest workoutPlanRequest) {
        WorkoutPlan workoutPlan = mapper.toEntity(findByIdForUser(workoutPlanId, userId));

        if(!userIsOwner(workoutPlan, userId)) {
            throw new OwnershipError("Workout Plan doesn't belong to this user");
        }

        workoutPlan.setName(workoutPlanRequest.name());
        workoutPlan.setSeries(convertSeriesRequestToEntities(workoutPlanRequest.series()));

        workoutPlan = workoutPlanRepository.save(workoutPlan);
        return mapper.toResponse(workoutPlan);
    }


    public void deleteWorkoutPlan(Long userId, Long workoutPlanId) {
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

    /**
     * Converts series requests from a WorkoutPlanExecutedDTO into Series entities.
     * <p>
     * This method processes both working series and warmup series requests (if present),
     * retrieves the associated exercises, and creates the corresponding Series entities.
     * The result is a unified set containing all executed series from the workout.
     * </p>
     *
     * @param series the request containing the exercise id and series
     * @return a list of all Series entities executed in the workout
     * @throws EntityNotFoundException if any exercise ID is not found
     */
    private List<PlannedSeries> convertSeriesRequestToEntities(List<PlannedSeriesRequest> series) {
        return series.stream().
                map(this::fromRequest).
                collect(Collectors.toList());
    }

    /**
     * Creates a PlannedSeries entity from a request DTO.
     * <p>
     * Constructs a new planned series instance with the exercise reference and
     * the weight and repetition values from the request.
     * </p>
     *
     * @param request the DTO containing the exercise id and series
     * @return a new PlannedSeries instance
     */
    private PlannedSeries fromRequest(PlannedSeriesRequest request) {
        return new PlannedSeries(exerciseService.findById(request.exerciseId()), request.series());
    }
}
