package cart.cartitem.application;

import java.util.List;
import java.util.stream.Collectors;

import cart.cartitem.application.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.application.dto.CartItemRequest;
import cart.cartitem.application.dto.CartItemResponse;
import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.member.domain.Member;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final ProductRepository productRepository, final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(member.getId());

        return cartItems.stream()
                .map(cartItem -> {
                    // TODO : batch 적용 해보기
                    Product findProduct = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Product가 존재하지 않습니다."));
                    return CartItemResponse.of(cartItem, findProduct);
                })
                .collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product findProduct = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Product가 존재하지 않습니다."));

        CartItem cartItem = new CartItem.Builder()
                .memberId(member.getId())
                .productId(findProduct.getId())
                .build();

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return savedCartItem.getId();
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem findCartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 CartItem이 존재하지 않습니다."));
        findCartItem.checkOwner(member.getId());

        if (request.getQuantity() == 0) {
            cartItemRepository.delete(findCartItem);
            return;
        }

        findCartItem.changeQuantity(request.getQuantity());
    }

    public void remove(Member member, Long id) {
        CartItem findCartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 CartItem이 존재하지 않습니다."));
        findCartItem.checkOwner(member.getId());

        cartItemRepository.delete(findCartItem);
    }
}
