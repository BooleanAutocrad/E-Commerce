package ideas.Ecommerce.handler;

import ideas.Ecommerce.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFound resourceNotFound){
        String message = resourceNotFound.getResourceName() + " not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(ExpiredException.class)
    public ResponseEntity<String> expiredException(ExpiredException expiredException){
        String message = expiredException.getResourceName() + " has Expired";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(IncorrectUserNameOrPasswordException.class)
    public ResponseEntity<String> handleResourceNotDeletedException(IncorrectUserNameOrPasswordException incorrectUserNameOrPasswordException) {
        String message = "Invalid Username or password";
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IncorrectPassword.class)
    public ResponseEntity<String> handleIncorrectPassword(IncorrectPassword incorrectPassword){
        return new ResponseEntity<String>("Invalid Username or password" , HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotDeleted.class)
    public ResponseEntity<String> handleResourceNotDeletedException(ResourceNotDeleted resourceNotDeleted) {
        String message = resourceNotDeleted.getResourceName() + " not deleted";
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgument.class)
    public ResponseEntity<String> handleWrongConditionException(IllegalArgument illegalArgument){
        String message = illegalArgument.getArgumentName() + " is not a valid condition";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UpdateNotPerformed.class)
    public ResponseEntity<String> updateNotPerformedException(UpdateNotPerformed updateNotPerformed){
        String message = updateNotPerformed.getErrorMessage() + " Something went wrong";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }


}
