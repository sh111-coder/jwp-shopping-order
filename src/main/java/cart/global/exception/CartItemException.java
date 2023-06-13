package cart.global.exception;

import cart.cartitem.domain.CartItem;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Long memberId) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + memberId);
        }
    }
}
