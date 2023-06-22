package cart.order.domain;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

import cart.common.BaseTimeEntity;

@Entity
@Table(name = "ORDERS")
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Long memberId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private int totalPrice;

    protected Order() {
    }

    private Order(final Long id, final Long memberId,
                  final List<OrderItem> orderItems, final int totalPrice) {
        this.id = id;
        this.memberId = memberId;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public static class Builder {

        private Long id;
        private Long memberId;
        private List<OrderItem> orderItems = new ArrayList<>();
        private int totalPrice;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder memberId(final Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder orderItems(final List<OrderItem> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public Builder totalPrice(final int totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Order build() {
            return new Order(id, memberId, orderItems, totalPrice);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
