package cart.global.exception.dto;

import cart.global.exception.ErrorCode;

public class ErrorResponse {

    private String code;
    private String message;

    private ErrorResponse() {
    }

    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        String code = errorCode.getCode();
        String message = errorCode.getMessage();
        return new ErrorResponse(code, message);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
