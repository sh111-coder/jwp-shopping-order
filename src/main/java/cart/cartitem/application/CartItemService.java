package cart.cartitem.application;

import java.util.List;
import java.util.stream.Collectors;

import cart.cartitem.application.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.application.dto.CartItemRequest;
import cart.cartitem.application.dto.CartItemResponse;
import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.cartitem.exception.CartItemNotFoundException;
import cart.global.exception.ErrorCode;
import cart.member.domain.Member;
import cart.member.domain.MemberRepository;
import cart.member.exception.MemberNotFoundException;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import cart.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final MemberRepository memberRepository, final ProductRepository productRepository,
                           final CartItemRepository cartItemRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(final String memberEmail) {
        Member findMember = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(findMember.getId());

        return cartItems.stream()
                .map(cartItem -> {
                    // TODO : batch 적용 해보기
                    Product findProduct = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(ErrorCode.NOT_FOUND_PRODUCT));
                    return CartItemResponse.of(cartItem, findProduct);
                })
                .collect(Collectors.toList());
    }

    public Long add(final String memberEmail, CartItemRequest cartItemRequest) {
        Member findMember = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        Product findProduct = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorCode.NOT_FOUND_PRODUCT));

        CartItem cartItem = new CartItem.Builder()
                .memberId(findMember.getId())
                .productId(findProduct.getId())
                .build();

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return savedCartItem.getId();
    }

    public void updateQuantity(final String memberEmail, Long id, CartItemQuantityUpdateRequest request) {
        Member findMember = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        CartItem findCartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorCode.NOT_FOUND_CART_ITEM));
        findCartItem.checkOwner(findMember.getId());

        if (request.getQuantity() == 0) {
            cartItemRepository.delete(findCartItem);
            return;
        }

        findCartItem.changeQuantity(request.getQuantity());
    }

    public void remove(final String memberEmail, Long id) {
        Member findMember = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        CartItem findCartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorCode.NOT_FOUND_CART_ITEM));
        findCartItem.checkOwner(findMember.getId());

        cartItemRepository.delete(findCartItem);
    }
}
