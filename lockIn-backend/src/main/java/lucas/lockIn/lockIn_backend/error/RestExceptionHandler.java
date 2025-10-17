package lucas.lockIn.lockIn_backend.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import lucas.lockIn.lockIn_backend.auth.exceptions.InvalidCredentialsException;
import lucas.lockIn.lockIn_backend.auth.exceptions.InvalidRefreshToken;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.workout.exceptions.WorkoutUnfinished;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

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
    public ResponseEntity<Object> handleWorkoutUnfinished(HttpServletRequest req, WorkoutUnfinished ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(ex.getMessage());

        return buildResponseEntity(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String message = "Error processing request";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                message = String.format("Invalid value '%s' for the field '%s'. Valid values: %s",
                        ife.getValue(),
                        ife.getPath().get(0).getFieldName(),
                        Arrays.toString(ife.getTargetType().getEnumConstants())
                );
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(message);

        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(ex.getMessage());

        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(HttpServletRequest req, InvalidCredentialsException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED);
        errorResponse.setMessage(ex.getMessage());

        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(InvalidRefreshToken.class)
    public ResponseEntity<Object> handleInvalidRefreshToken(HttpServletRequest req, InvalidRefreshToken ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED);
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse){
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}