package cart.coupon.domain;

import javax.persistence.*;

import cart.member.domain.Member;

@Entity
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "coupon_type")
    private CouponType type;

    @Column(name = "coupon_value")
    private int value;

    protected Coupon() {
    }

    private Coupon(final Long id, final Member member,
                  final CouponType type, final int value) {
        this.id = id;
        this.member = member;
        this.type = type;
        this.value = value;
    }

    public static class Builder {

        private Long id;
        private Member member;
        private CouponType type;
        private int value;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder member(final Member member) {
            this.member = member;
            return this;
        }

        public Builder type(final CouponType type) {
            this.type = type;
            return this;
        }

        public Builder value(final int value) {
            this.value = value;
            return this;
        }

        public Coupon build() {
            return new Coupon(id, member, type, value);
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public CouponType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
