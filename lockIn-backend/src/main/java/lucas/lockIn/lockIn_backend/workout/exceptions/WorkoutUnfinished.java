package lucas.lockIn.lockIn_backend.workout.exceptions;

public class WorkoutUnfinished extends RuntimeException {
    public WorkoutUnfinished(String message) {
        super(message);
    }
}
