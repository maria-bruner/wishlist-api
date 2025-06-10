package com.br.netshoes.wishlist_api.application.service;

import com.br.netshoes.wishlist_api.application.mapper.WishlistMapper;
import com.br.netshoes.wishlist_api.domain.exception.ProductAlreadyInWishlistException;
import com.br.netshoes.wishlist_api.domain.exception.ProductNotFoundInWishlistException;
import com.br.netshoes.wishlist_api.domain.exception.WishlistLimitExceededException;
import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import com.br.netshoes.wishlist_api.domain.port.input.WishlistServicePort;
import com.br.netshoes.wishlist_api.domain.port.output.WishlistRepositoryPort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService implements WishlistServicePort {

    private final WishlistRepositoryPort wishlistRepository;
    private final MeterRegistry meterRegistry;
    private final WishlistMapper mapper;
    private final Counter wishlistAdditionsTotal;

    private static final int WISHLIST_LIMIT = 20;

    public WishlistService(WishlistRepositoryPort wishlistRepository, MeterRegistry meterRegistry, WishlistMapper mapper) {
        this.wishlistRepository = wishlistRepository;
        this.meterRegistry = meterRegistry;
        this.mapper = mapper;

        this.wishlistAdditionsTotal = Counter.builder("wishlist_total_additions_total")
                .description("Total de produtos adicionados na Wishlist")
                .register(meterRegistry);
    }

    @Override
    public Wishlist addToWishlist(Wishlist wishlist) {
        String userId = wishlist.getUserId();
        String productId = wishlist.getProductId();

        long currentItemsInWishlist = wishlistRepository.countByUserId(userId);

        if (currentItemsInWishlist >= WISHLIST_LIMIT) {
            throw new WishlistLimitExceededException("A wishlist do usuário " + userId + " já atingiu o limite máximo de " + WISHLIST_LIMIT + " produtos.");
        }

        boolean exists = wishlistRepository.existsByUserIdAndProductId(userId, productId);
        if (exists) {
            throw new ProductAlreadyInWishlistException("Produto já está na wishlist para o usuário " + userId + " e produto " + productId);
        }

        Wishlist saved = wishlistRepository.save(wishlist);

        wishlistAdditionsTotal.increment();

        meterRegistry.counter(
                "wishlist_product_additions_total",
                Tags.of("product_id", productId, "product_name", wishlist.getProductName(), "user_id", userId)
        ).increment();

        long newCount = wishlistRepository.countByUserId(userId);
        meterRegistry.gauge("wishlist_user_item_count", Tags.of("user_id", userId), newCount);

        return saved;
    }

    @Override
    public Page<Wishlist> getWishlistByUser(String userId, Pageable pageable) {
        long currentCount = wishlistRepository.countByUserId(userId);
        meterRegistry.gauge("wishlist_user_item_count", Tags.of("user_id", userId), currentCount);

        return wishlistRepository.findByUserId(userId, pageable);
    }

    @Override
    public void removeFromWishlist(String userId, String productId) {
        boolean exists = wishlistRepository.existsByUserIdAndProductId(userId, productId);
        if (!exists) {
            throw new ProductNotFoundInWishlistException("Produto com ID " + productId + " não encontrado na wishlist do usuário " + userId);
        }
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);

        long newCount = wishlistRepository.countByUserId(userId);
        meterRegistry.gauge("wishlist_user_item_count", Tags.of("user_id", userId), newCount);
    }

    @Override
    public boolean isProductInWishlist(String userId, String productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }
}