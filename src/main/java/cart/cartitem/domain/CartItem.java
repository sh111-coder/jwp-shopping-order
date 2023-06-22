package cart.cartitem.domain;

import java.util.Objects;

import javax.persistence.*;

import cart.cartitem.exception.CartItemException;
import cart.common.BaseTimeEntity;

@Entity
public class CartItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;

    protected CartItem() {
    }

    private CartItem(final Long id, final Long memberId,
                    final Long productId, final int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static class Builder {

        private Long id;
        private Long memberId;
        private Long productId;
        private int quantity = 1;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder memberId(final Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder productId(final Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(final int quantity) {
            this.quantity = quantity;
            return this;
        }

        public CartItem build() {
            return new CartItem(id, memberId, productId, quantity);
        }
    }

    public void checkOwner(Long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new CartItemException.IllegalMember(this, memberId);
        }
    }

    public void changeQuantity(int quantityToChange) {
        this.quantity = quantityToChange;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
