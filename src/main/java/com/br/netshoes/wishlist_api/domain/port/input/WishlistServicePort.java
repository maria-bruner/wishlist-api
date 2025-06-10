package com.br.netshoes.wishlist_api.domain.port.input;

import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistServicePort {

    Wishlist addToWishlist(Wishlist wishlist);

    void removeFromWishlist(String userId, String productId);

    Page<Wishlist> getWishlistByUser(String userId, Pageable pageable);

    boolean isProductInWishlist(String userId, String productId);
}
