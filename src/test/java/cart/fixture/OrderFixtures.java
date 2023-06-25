package cart.fixture;

import static cart.fixture.CartItemFixtures.*;
import static cart.fixture.CouponFixtures.FIX_1000;
import static cart.fixture.CouponFixtures.RATE_5;
import static cart.fixture.MemberFixtures.MEMBER_A;
import static cart.fixture.ProductFixtures.*;

import java.util.List;

import cart.cartitem.domain.CartItem;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderItems;
import cart.product.domain.Product;

public class OrderFixtures {

    public static class Member_A_Order1 {
        public static Order ENTITY() {
            OrderItems orderItems = new OrderItems(List.of(Member_A_Discounted_OrderItem1.ENTITY(), Member_A_Discounted_OrderItem2.ENTITY()));
            return Order.createOrder(MEMBER_A.ID, orderItems);
        }
    }

    public static class Member_A_Order2 {
        public static Order ENTITY() {
            OrderItems orderItems = new OrderItems(List.of(Member_A_Not_Discounted_OrderItem3.ENTITY()));
            return Order.createOrder(MEMBER_A.ID, orderItems);
        }
    }

    public static class Member_A_Discounted_OrderItem1 {

        public static final Long COUPON_ID = RATE_5.ID;
        public static final Product PRODUCT = CHICKEN.ENTITY();
        public static final CartItem CART_ITEM = MEMBER_A_CART_ITEM1.ENTITY();

        public static OrderItem ENTITY() {
            return OrderItem.createDiscountedOrderItem(COUPON_ID, PRODUCT, CART_ITEM);
        }
    }

    public static class Member_A_Discounted_OrderItem2 {

        public static final Long COUPON_ID = FIX_1000.ID;
        public static final Product PRODUCT = SALAD.ENTITY();
        public static final CartItem CART_ITEM = MEMBER_A_CART_ITEM2.ENTITY();

        public static OrderItem ENTITY() {
            return OrderItem.createDiscountedOrderItem(COUPON_ID, PRODUCT, CART_ITEM);
        }
    }

    public static class Member_A_Not_Discounted_OrderItem3 {

        public static final Product PRODUCT = PIZZA.ENTITY();
        public static final CartItem CART_ITEM = MEMBER_A_CART_ITEM3.ENTITY();

        public static OrderItem ENTITY() {
            return OrderItem.createNotDiscountedOrderItem(PRODUCT, CART_ITEM);
        }
    }
}
