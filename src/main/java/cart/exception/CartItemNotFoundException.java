package cart.exception;

public class CartItemNotFoundException extends NotFoundException {

    public CartItemNotFoundException(String message) {
        super(message);
    }
}