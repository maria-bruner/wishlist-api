package com.br.netshoes.wishlist_api.infra.adapter.input;

import com.br.netshoes.wishlist_api.application.dto.WishlistRequest;
import com.br.netshoes.wishlist_api.application.dto.WishlistResponse;
import com.br.netshoes.wishlist_api.application.mapper.WishlistMapper;
import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import com.br.netshoes.wishlist_api.domain.port.input.WishlistServicePort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistServicePort wishlistService;
    private final WishlistMapper mapper;

    public WishlistController(WishlistServicePort wishlistService, WishlistMapper mapper) {
        this.wishlistService = wishlistService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<WishlistResponse> addToWishlist(@Valid @RequestBody WishlistRequest request) {
        Wishlist wishlist = mapper.toDomain(request);
        Wishlist saved = wishlistService.addToWishlist(wishlist);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<WishlistResponse>> getWishlistByUser(
            @PathVariable String userId,
            @PageableDefault(size = 20, page = 0, sort = "productName") Pageable pageable) {

        Page<Wishlist> wishlistPage = wishlistService.getWishlistByUser(userId, pageable);

        Page<WishlistResponse> responsePage = wishlistPage.map(mapper::toResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{userId}/contains/{productId}")
    public ResponseEntity<Boolean> isProductInWishlist(@PathVariable String userId, @PathVariable String productId) {
        return ResponseEntity.ok(wishlistService.isProductInWishlist(userId, productId));
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable String userId, @PathVariable String productId) {
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
}