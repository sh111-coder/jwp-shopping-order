package cart.domain.cartitem.persistence;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.member.domain.Member;
import cart.domain.product.domain.Product;
import cart.global.exception.CartItemNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    public List<CartItem> findDescByMemberId(Long memberId) {
        String sql = JOIN_SQL + "WHERE cart_item.member_id = ? ORDER BY cart_item.id DESC";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<CartItem> selectAllByMemberId(Long memberId) {
        String sql = JOIN_SQL + "WHERE member_id=?";
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
        String sql = JOIN_SQL + "WHERE cart_item.id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream()
                .findAny()
                .orElseThrow(() -> new CartItemNotFoundException("장바구니 상품 ID에 해당하는 장바구니 상품을 찾을 수 없습니다."));
    }


    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public Boolean isNotExistById(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM cart_item WHERE id=?)";
        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByIds(List<Long> ids) {
        String sql = "DELETE FROM cart_item WHERE id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

