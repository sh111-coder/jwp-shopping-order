package cart.cartitem.domain;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    CartItem save(CartItem cartItem);

    Optional<CartItem> findById(Long id);

    List<CartItem> findAllByMemberIdAndIds(Long memberId, List<Long> ids);

    List<CartItem> findAllByMemberId(Long memberId);

    void deleteByMemberIdAndProductId(Long memberId, Long productId);

    void delete(CartItem cartItem);
}
