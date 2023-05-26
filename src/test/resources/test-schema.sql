CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cash     BIGINT       NOT NULL DEFAULT 5000
    );

CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT  NOT NULL,
    product_id BIGINT  NOT NULL,
    quantity   INT     NOT NULL,
    is_ordered BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id)
    );

CREATE TABLE IF NOT EXISTS cart_order
(
    id          BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    total_price BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS order_item
(
    id            BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cart_item_id  BIGINT NOT NULL,
    cart_order_id BIGINT NOT NULL,
    FOREIGN KEY (cart_item_id) REFERENCES cart_item (id),
    FOREIGN KEY (cart_order_id) REFERENCES cart_order (id) ON DELETE CASCADE
    );