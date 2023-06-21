package cart.fixture;

import static cart.fixture.MemberFixtures.MEMBER_A;
import static cart.fixture.ProductFixtures.CHICKEN;
import static cart.fixture.ProductFixtures.SALAD;

import cart.cartitem.domain.CartItem;

public class CartItemFixtures {

    public static class MEMBER_A_CART_ITEM1 {
        public static final Long ID = 1L;
        public static final Long MEMBER_ID = MEMBER_A.ID;
        public static final Long PRODUCT_ID = CHICKEN.ID;
        public static final int QUANTITY = 2;

        public static CartItem ENTITY() {
            return new CartItem.Builder()
                    .id(ID)
                    .memberId(MEMBER_ID)
                    .productId(PRODUCT_ID)
                    .quantity(QUANTITY)
                    .build();
        }
    }

    public static class MEMBER_A_CART_ITEM2 {
        public static final Long ID = 2L;
        public static final Long MEMBER_ID = MEMBER_A.ID;
        public static final Long PRODUCT_ID = SALAD.ID;
        public static final int QUANTITY = 4;

        public static CartItem ENTITY() {
            return new CartItem.Builder()
                    .id(ID)
                    .memberId(MEMBER_ID)
                    .productId(PRODUCT_ID)
                    .quantity(QUANTITY)
                    .build();
        }
    }
}
