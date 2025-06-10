package com.br.netshoes.wishlist_api.domain.port.output;

import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WishlistRepositoryPort {

    Wishlist save(Wishlist wishlist);

    void deleteByUserIdAndProductId(String userId, String productId);

    Page<Wishlist> findByUserId(String userId, Pageable pageable);

    boolean existsByUserIdAndProductId(String userId, String productId);

    Optional<Wishlist> findByUserIdAndProductId(String userId, String productId);

    long countByUserId(String userId);
}