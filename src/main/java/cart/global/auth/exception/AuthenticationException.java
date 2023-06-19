package cart.global.auth.exception;

import cart.global.exception.CustomException;
import cart.global.exception.ErrorCode;

public class AuthenticationException extends CustomException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
