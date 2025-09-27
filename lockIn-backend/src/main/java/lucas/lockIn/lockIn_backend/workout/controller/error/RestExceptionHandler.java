package lucas.lockIn.lockIn_backend.workout.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.exceptions.WorkoutUnfinished;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(HttpServletRequest req, EntityNotFoundException ex){

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND);
        errorResponse.setMessage(ex.getMessage());

        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(WorkoutUnfinished.class)
    public ResponseEntity<Object>  handleWorkoutUnfinished(HttpServletRequest req, WorkoutUnfinished ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(ex.getMessage());

        return buildResponseEntity(errorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse){
        return new ResponseEntity<Object>(errorResponse, errorResponse.getStatus());
    }
}
