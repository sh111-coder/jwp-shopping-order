package cart.fixture;

import static cart.fixture.MemberFixtures.MEMBER_A;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.CouponType;
import cart.member.domain.Member;

public class CouponFixtures {

    public static class RATE_5 {

        public static final Long ID = 1L;
        public static final Member MEMBER = MEMBER_A.ENTITY();
        public static final CouponType TYPE = CouponType.RATE;
        public static final int VALUE = 5;

        public static Coupon ENTITY() {
            return new Coupon.Builder()
                    .id(ID)
                    .member(MEMBER)
                    .type(TYPE)
                    .value(VALUE)
                    .build();
        }
    }

    public static class FIX_1000 {

        public static final Long ID = 2L;
        public static final Member MEMBER = MEMBER_A.ENTITY();
        public static final CouponType TYPE = CouponType.FIX;
        public static final int VALUE = 1000;

        public static Coupon ENTITY() {
            return new Coupon.Builder()
                    .id(ID)
                    .member(MEMBER)
                    .type(TYPE)
                    .value(VALUE)
                    .build();
        }
    }
}
