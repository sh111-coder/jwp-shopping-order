package cart.member.exception;

import cart.global.exception.ErrorCode;
import cart.global.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
