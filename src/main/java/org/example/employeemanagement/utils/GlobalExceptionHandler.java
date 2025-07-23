    package org.example.employeemanagement.utils;

    import org.example.employeemanagement.dto.ErrorResponse;
    import org.springframework.dao.DataAccessException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;

    import java.time.LocalDateTime;

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler({UsernameNotFoundException.class, RuntimeException.class})
        public ResponseEntity<ErrorResponse> handleResourceNotFound(RuntimeException ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    ex.getMessage(),
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // Returns 404
        }

        @ExceptionHandler(DataAccessException.class)
        public ResponseEntity<ErrorResponse> handleDatabaseException(DataAccessException ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "A database error occurred. The operation could not be completed.",
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // Returns 500
        }
    }
