package ericarfs.socialmedia.exceptions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException e,
			HttpServletRequest request) {
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(err, status);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> handleDatabaseException(DatabaseException e, HttpServletRequest request) {
		String error = "Database error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(err, status);
	}

	@ExceptionHandler(PermissionDeniedException.class)
	public ResponseEntity<StandardError> handlePermissionDeniedException(PermissionDeniedException e,
			HttpServletRequest request) {
		String error = "Permission denied";
		HttpStatus status = HttpStatus.FORBIDDEN;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(err, status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex) {
		Map<String, Object> body = new LinkedHashMap<>();

		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.put("error", "Internal Server Error");
		body.put("message", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
