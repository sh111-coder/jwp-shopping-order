package cart.cartitem.exception;

import cart.global.exception.ErrorCode;
import cart.global.exception.NotFoundException;

public class CartItemNotFoundException extends NotFoundException {

    public CartItemNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
