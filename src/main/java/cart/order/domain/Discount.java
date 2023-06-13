package cart.order.domain;

import javax.persistence.*;

@Entity
public class Discount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long id;
    private Long orderItemId;
    private Long couponId;
    private int discountPrice;

    protected Discount() {
    }

    private Discount(final Long id, final Long orderItemId,
                     final Long couponId, final int discountPrice) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.couponId = couponId;
        this.discountPrice = discountPrice;
    }

    public static class Builder {

        private Long id;
        private Long orderItemId;
        private Long couponId;
        private int discountPrice;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder orderItemId(final Long orderItemId) {
            this.orderItemId = orderItemId;
            return this;
        }

        public Builder couponId(final Long couponId) {
            this.couponId = couponId;
            return this;
        }

        public Builder discountPrice(final int discountPrice) {
            this.discountPrice = discountPrice;
            return this;
        }

        public Discount build() {
            return new Discount(id, orderItemId, couponId, discountPrice);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
