package cart.order.domain;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Long memberId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private int totalPrice;

    private LocalDateTime createdAt;

    protected Order() {
    }

    private Order(final Long id, final Long memberId,
                  final List<OrderItem> orderItems, final int totalPrice,
                  final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public static class Builder {

        private Long id;
        private Long memberId;
        private List<OrderItem> orderItems = new ArrayList<>();
        private int totalPrice;
        private LocalDateTime createdAt;

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

        public Builder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Order build() {
            return new Order(id, memberId, orderItems, totalPrice, createdAt);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
