package cart.cartitem.persistence;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepositoryImpl implements CartItemRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public CartItem save(CartItem cartItem) {
        em.persist(cartItem);
        return cartItem;
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        CartItem findCartItem = em.find(CartItem.class, id);
        return Optional.ofNullable(findCartItem);
    }

    @Override
    public List<CartItem> findAllByMemberIdAndIds(Long memberId, List<Long> ids) {
        return em.createQuery("select c from CartItem c where c.id in :ids and c.memberId = :memberId", CartItem.class)
                .setParameter("ids", ids)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<CartItem> findAllByMemberId(Long memberId) {
        return em.createQuery("select c from CartItem c where c.memberId = :memberId", CartItem.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public void deleteByMemberIdAndProductId(Long memberId, Long productId) {
        em.createQuery("delete from CartItem c where c.memberId = :memberId and c.productId = :productId")
                .setParameter("memberId", memberId)
                .setParameter("productId", productId)
                .executeUpdate();
    }

    @Override
    public void delete(CartItem cartItem) {
        em.remove(cartItem);
    }
}
