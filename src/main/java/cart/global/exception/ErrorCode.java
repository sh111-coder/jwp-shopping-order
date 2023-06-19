package cart.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    /**
     * AUTH
     */
    NOT_EXISTS_AUTHORIZATION_HEADER("AU_001", HttpStatus.UNAUTHORIZED, "Authorization 헤더가 존재하지 않습니다."),
    NOT_EXISTS_AUTHORIZATION_PREFIX("AU_002", HttpStatus.UNAUTHORIZED, "인증 방식의 PREFIX가 존재하지 않습니다."),
    AUTH_MEMBER_FAIL("AU_003", HttpStatus.UNAUTHORIZED, "인증 정보가 올바르지 않습니다."),

    /**
     * MEMBER
     */
    NOT_FOUND_MEMBER("ME_001", HttpStatus.NOT_FOUND, "해당하는 Memeber를 찾을 수 없습니다."),

    /**
     * PRODUCT
     */
    NOT_FOUND_PRODUCT("PR_001", HttpStatus.NOT_FOUND, "해당하는 Product를 찾을 수 없습니다."),

    /**
     * CART ITEM
     */
    NOT_FOUND_CART_ITEM("CI_001", HttpStatus.NOT_FOUND, "해당하는 Cart Item을 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
