package cart.product.domain;

import javax.persistence.*;

@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;
    private int price;
    private String imageUrl;

    protected Product() {
    }

    private Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static class Builder {

        private Long id;
        private String name;
        private int price;
        private String imageUrl;

        public Builder id(final Long id) {
            this.id = id;
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

        public Product build() {
            return new Product(id, name, price, imageUrl);
        }
    }

    public void changeName(String nameToChange) {
        this.name = nameToChange;
    }

    public void changePrice(int priceToChange) {
        this.price = priceToChange;
    }

    public void changeImageUrl(String imageUrlToChange) {
        this.imageUrl = imageUrlToChange;
    }

    public Long getId() {
        return id;
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
}
