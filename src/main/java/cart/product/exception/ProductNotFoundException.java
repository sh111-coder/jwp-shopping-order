package cart.product.exception;

import cart.global.exception.ErrorCode;
import cart.global.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
