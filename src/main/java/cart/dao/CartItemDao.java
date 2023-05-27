package cart.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private static final String JOIN_SQL = "SELECT cart_item.id, cart_item.member_id, member.email, member.password, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
            "FROM cart_item " +
            "INNER JOIN member ON cart_item.member_id = member.id " +
            "INNER JOIN product ON cart_item.product_id = product.id ";

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<CartItem> rowMapper = (rs, rowNum) -> {
        String email = rs.getString("email");
        Long productId = rs.getLong("product.id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        Long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("cart_item.quantity");
        long memberId = rs.getLong("cart_item.member_id");
        String password = rs.getString("member.password");
        Member member = new Member(memberId, email, password);
        Product product = new Product(productId, name, price, imageUrl);
        return new CartItem(cartItemId, quantity, product, member);
    };

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.password, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<CartItem> selectByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = JOIN_SQL + "WHERE member_id=? AND product_id=?";
        return jdbcTemplate.query(sql, rowMapper, memberId, productId).stream().findAny();
    }

    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public CartItem findById(Long id) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Member member = new Member(memberId, email, null);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        });
        return cartItems.isEmpty() ? null : cartItems.get(0);
    }


    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

