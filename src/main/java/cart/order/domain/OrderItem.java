package cart.order.domain;

import javax.persistence.*;

import cart.common.BaseTimeEntity;

@Entity
public class OrderItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Boolean isDiscounted;

    @Column(name = "order_item_name")
    private String name;

    private int price;

    private String imageUrl;

    private int quantity;

    protected OrderItem() {
    }

    private OrderItem(final Long id, final Long productId,
                      final Order order, final Boolean isDiscounted,
                      final String name, final int price,
                      final String imageUrl, final int quantity) {
        this.id = id;
        this.productId = productId;
        this.order = order;
        this.isDiscounted = isDiscounted;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static class Builder {

        private Long id;
        private Long productId;
        private Order order;
        private Boolean isDiscounted;
        private String name;
        private int price;
        private String imageUrl;
        private int quantity;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder productId(final Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder order(final Order order) {
            this.order = order;
            return this;
        }

        public Builder isDiscounted(final Boolean isDiscounted) {
            this.isDiscounted = isDiscounted;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder price(final int price) {
            this.price = price;
            return this;
        }

        public Builder imageUrl(final String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder quantity(final int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(id, productId, order, isDiscounted, name, price, imageUrl, quantity);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Order getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
