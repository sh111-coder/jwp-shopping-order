package cart.fixture;

import cart.product.domain.Product;

public class ProductFixtures {

    public static class CHICKEN {
        public static final Long ID = 1L;
        public static final String NAME = "치킨";
        public static final int PRICE = 10000;
        public static final String IMAGE_URL = "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80";

        public static Product ENTITY() {
            return new Product.Builder()
                    .id(ID)
                    .name(NAME)
                    .price(PRICE)
                    .imageUrl(IMAGE_URL)
                    .build();
        }
    }

    public static class SALAD {
        public static final Long ID = 2L;
        public static final String NAME = "샐러드";
        public static final int PRICE = 20000;
        public static final String IMAGE_URL = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80";

        public static Product ENTITY() {
            return new Product.Builder()
                    .id(ID)
                    .name(NAME)
                    .price(PRICE)
                    .imageUrl(IMAGE_URL)
                    .build();
        }
    }

    public static class PIZZA {
        public static final Long ID = 3L;
        public static final String NAME = "피자";
        public static final int PRICE = 13000;
        public static final String IMAGE_URL = "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80";

        public static Product ENTITY() {
            return new Product.Builder()
                    .id(ID)
                    .name(NAME)
                    .price(PRICE)
                    .imageUrl(IMAGE_URL)
                    .build();
        }
    }
}
