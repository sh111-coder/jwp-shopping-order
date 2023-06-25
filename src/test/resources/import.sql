INSERT INTO product (product_name, price, image_url, created_at, updated_at) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', current_timestamp, current_timestamp);
INSERT INTO product (product_name, price, image_url, created_at, updated_at) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', current_timestamp, current_timestamp);
INSERT INTO product (product_name, price, image_url, created_at, updated_at) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80', current_timestamp, current_timestamp);

INSERT INTO member (email, password, created_at, updated_at) VALUES ('a@a.com', '1234', current_timestamp, current_timestamp);
INSERT INTO member (email, password, created_at, updated_at) VALUES ('b@b.com', '1234', current_timestamp, current_timestamp);

INSERT INTO coupon (member_id, coupon_type, coupon_value, is_used, created_at, updated_at) VALUES (1, 'RATE', 5, true, current_timestamp, current_timestamp);
INSERT INTO coupon (member_id, coupon_type, coupon_value, is_used, created_at, updated_at) VALUES (1, 'FIX', 1000, true, current_timestamp, current_timestamp);
INSERT INTO coupon (member_id, coupon_type, coupon_value, is_used, created_at, updated_at) VALUES (2, 'RATE', 5, false, current_timestamp, current_timestamp);
INSERT INTO coupon (member_id, coupon_type, coupon_value, is_used, created_at, updated_at) VALUES (2, 'FIX', 1000, false, current_timestamp, current_timestamp);

INSERT INTO cart_item (member_id, product_id, quantity, created_at, updated_at) VALUES (1, 1, 2, current_timestamp, current_timestamp);
INSERT INTO cart_item (member_id, product_id, quantity, created_at, updated_at) VALUES (1, 2, 4, current_timestamp, current_timestamp);
INSERT INTO cart_item (member_id, product_id, quantity, created_at, updated_at) VALUES (1, 3, 3, current_timestamp, current_timestamp);

INSERT INTO cart_item (member_id, product_id, quantity, created_at, updated_at) VALUES (2, 3, 5, current_timestamp, current_timestamp);

INSERT INTO orders (member_id, total_price, created_at, updated_at) VALUES (1, 100000, current_timestamp, current_timestamp);
INSERT INTO orders (member_id, total_price, created_at, updated_at) VALUES (2, 39000, current_timestamp, current_timestamp);

INSERT INTO order_item (order_id, product_id, coupon_id, is_discounted, order_item_name, order_item_price, order_item_image_url, quantity, order_price, created_at, updated_at)
        VALUES (1, 1, 1, true, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80',
                2, 19000, current_timestamp, current_timestamp);

INSERT INTO order_item (order_id, product_id, coupon_id, is_discounted, order_item_name, order_item_price, order_item_image_url, quantity, order_price, created_at, updated_at)
VALUES (1, 2, 2, true, '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80',
        4, 79000, current_timestamp, current_timestamp);

INSERT INTO order_item (order_id, product_id, coupon_id, is_discounted, order_item_name, order_item_price, order_item_image_url, quantity, order_price, created_at, updated_at)
VALUES (2, 3, null, false, '피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80',
        3, 39000, current_timestamp, current_timestamp);
