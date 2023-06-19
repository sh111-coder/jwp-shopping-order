package cart.global.exception;

import cart.cartitem.exception.CartItemException;
import cart.global.auth.exception.AuthenticationException;
import cart.global.exception.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String UNEXPECTED_EXCEPTION_MESSAGE = "관리자에게 문의하세요.";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(AuthenticationException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.error(errorCode.getMessage(), exception);
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        System.out.println("errorResponse = " + errorResponse);
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.error(errorCode.getMessage(), exception);
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUnexpectedException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError().body(UNEXPECTED_EXCEPTION_MESSAGE);
    }
}
