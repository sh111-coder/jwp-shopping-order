package cart.fixture;

import static cart.fixture.CartItemFixtures.MEMBER_A_CART_ITEM1;
import static cart.fixture.CartItemFixtures.MEMBER_A_CART_ITEM2;
import static cart.fixture.ProductFixtures.CHICKEN;
import static cart.fixture.ProductFixtures.SALAD;

import java.util.List;

import cart.cartitem.application.dto.CartItemResponse;

public class MemberFixtures {

    public static class MEMBER_A {

        public static final Long ID = 1L;
        public static final String EMAIL = "a@a.com";
        public static final String PASSWORD = "1234";

        public static final List<CartItemResponse> CART_ITEM_RESPONSES = List.of(
                CartItemResponse.of(MEMBER_A_CART_ITEM1.ENTITY(), CHICKEN.ENTITY()),
                CartItemResponse.of(MEMBER_A_CART_ITEM2.ENTITY(), SALAD.ENTITY())
        );
    }
}
