package ru.skillbox.group39.socialnetwork.notifications.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.skillbox.group39.socialnetwork.notifications.exception.exceptions.*;

@Slf4j
@ControllerAdvice
public class FeignClientExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ForbiddenException.class})
	public ResponseEntity<ErrorResponse> handleForbiddenException(@NotNull ForbiddenException exception, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap ForbiddenException");
		return new ResponseEntity<>(new ErrorResponse(
				exception.getMessage(), HttpStatus.valueOf(403)
		),
				HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({EmailNotUniqueException.class})
	public ResponseEntity<ErrorResponse> handleEmailNotUniqueException(@NotNull EmailNotUniqueException exception, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap EmailNotUniqueException");
		return new ResponseEntity<>(new ErrorResponse(
				exception.getMessage(), HttpStatus.valueOf(409)
		),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler({EmailNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleEmailNotUniqueException(@NotNull EmailNotFoundException exception, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap EmailNotFoundException");
		return new ResponseEntity<>(new ErrorResponse(
				exception.getMessage(), HttpStatus.valueOf(401)
		),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({SettingsNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleSettingsNotUniqueException(@NotNull SettingsNotFoundException exception, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap SettingsNotFoundException");
		return new ResponseEntity<>(new ErrorResponse(
				exception.getMessage(), HttpStatus.valueOf(400)
		),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({EmailIsBlankException.class})
	public ResponseEntity<ErrorResponse> handleEmailIsBlankException(@NotNull EmailIsBlankException exception, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap EmailIsBlankException");
		return new ResponseEntity<>(new ErrorResponse(
				exception.getMessage(), HttpStatus.valueOf(406)
		),
				HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler({BadRequestException.class})
	public ResponseEntity<ErrorResponse> handleEmailIsBlankException(@NotNull BadRequestException exception, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap BadRequestException");
		return new ResponseEntity<>(new ErrorResponse(
				exception.getMessage(), HttpStatus.valueOf(400)
		),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({RequestRejectedException.class})
	public ResponseEntity<ErrorResponse> handleRejectedException(@NotNull RequestRejectedException e, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap RequestRejectedException");
		return new ResponseEntity<>(new ErrorResponse(
				e.getMessage(), HttpStatus.valueOf(500)
		),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ConnectException.class})
	public ResponseEntity<ErrorResponse> handleConnectException(@NotNull ConnectException e, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap ConnectException");
		return new ResponseEntity<>(new ErrorResponse(
				e.getMessage(), HttpStatus.valueOf(500)
		),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({CallNotPermittedException.class})
	public ResponseEntity<ErrorResponse> handleCallNotPermittedException(@NotNull CallNotPermittedException e, WebRequest request) {
		log.error(" ! FeignClientExceptionHandler trap CallNotPermittedException");
		return new ResponseEntity<>(new ErrorResponse(
				e.getMessage(), HttpStatus.valueOf(500)
		),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
