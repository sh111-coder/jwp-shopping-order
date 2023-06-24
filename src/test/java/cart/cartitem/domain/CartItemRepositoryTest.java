package cart.cartitem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import cart.cartitem.persistence.CartItemRepositoryImpl;
import cart.fixture.CartItemFixtures;
import cart.fixture.MemberFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(CartItemRepositoryImpl.class)
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("MemberId와 CartItemId 리스트를 받아서 해당하는 CartItem을 조회한다.")
    void findAllByMemberIdAndIds() {
        // given
        Long memberId = MemberFixtures.MEMBER_A.ID;
        List<Long> cartItemIds = List.of(CartItemFixtures.MEMBER_A_CART_ITEM1.ID, CartItemFixtures.MEMBER_A_CART_ITEM2.ID);
        List<CartItem> expectedCartItems = List.of(CartItemFixtures.MEMBER_A_CART_ITEM1.ENTITY(), CartItemFixtures.MEMBER_A_CART_ITEM2.ENTITY());

        // when
        List<CartItem> findCartItems = cartItemRepository.findAllByMemberIdAndIds(memberId, cartItemIds);

        // then
        assertThat(findCartItems).usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt")
                .isEqualTo(expectedCartItems);
    }
}
