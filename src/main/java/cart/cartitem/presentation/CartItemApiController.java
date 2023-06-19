package cart.cartitem.presentation;

import java.net.URI;
import java.util.List;

import cart.cartitem.application.CartItemService;
import cart.cartitem.application.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.application.dto.CartItemRequest;
import cart.cartitem.application.dto.CartItemResponse;
import cart.global.auth.AuthMember;
import cart.global.auth.AuthPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@AuthPrincipal AuthMember authMember) {
        return ResponseEntity.ok(cartItemService.findByMember(authMember.getEmail()));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(@AuthPrincipal AuthMember authMember, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(authMember.getEmail(), cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@AuthPrincipal AuthMember authMember, @PathVariable Long id, @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(authMember.getEmail(), id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@AuthPrincipal AuthMember authMember, @PathVariable Long id) {
        cartItemService.remove(authMember.getEmail(), id);

        return ResponseEntity.noContent().build();
    }
}
