package com.br.netshoes.wishlist_api.application.service;

import com.br.netshoes.wishlist_api.application.mapper.WishlistMapper;
import com.br.netshoes.wishlist_api.domain.exception.ProductAlreadyInWishlistException;
import com.br.netshoes.wishlist_api.domain.exception.ProductNotFoundInWishlistException;
import com.br.netshoes.wishlist_api.domain.exception.WishlistLimitExceededException;
import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import com.br.netshoes.wishlist_api.domain.port.output.WishlistRepositoryPort;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    private WishlistRepositoryPort wishlistRepository;
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        wishlistRepository = mock(WishlistRepositoryPort.class);
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        WishlistMapper mapper = new WishlistMapper();

        wishlistService = new WishlistService(wishlistRepository, meterRegistry, mapper);
    }

    private Wishlist createWishlist() {
        return Wishlist.builder()
                .userId("user1")
                .productId("prod1")
                .productName("Tênis Esportivo")
                .productValue(BigDecimal.valueOf(199.90))
                .urlImage("https://img.com/prod1.jpg")
                .build();
    }

    @Test
    void shouldAddToWishlistSuccessfully() {
        Wishlist wishlist = createWishlist();

        when(wishlistRepository.countByUserId("user1")).thenReturn(0L);
        when(wishlistRepository.existsByUserIdAndProductId("user1", "prod1")).thenReturn(false);
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);

        Wishlist result = wishlistService.addToWishlist(wishlist);

        assertEquals("prod1", result.getProductId());
        assertEquals("Tênis Esportivo", result.getProductName());
        assertEquals(BigDecimal.valueOf(199.90), result.getProductValue());
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void shouldThrowWishlistLimitExceededException() {
        Wishlist wishlist = createWishlist();

        when(wishlistRepository.countByUserId("user1")).thenReturn(20L);

        assertThrows(WishlistLimitExceededException.class, () -> wishlistService.addToWishlist(wishlist));
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void shouldThrowProductAlreadyInWishlistException() {
        Wishlist wishlist = createWishlist();

        when(wishlistRepository.countByUserId("user1")).thenReturn(5L);
        when(wishlistRepository.existsByUserIdAndProductId("user1", "prod1")).thenReturn(true);

        assertThrows(ProductAlreadyInWishlistException.class, () -> wishlistService.addToWishlist(wishlist));
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void shouldGetWishlistByUser() {
        String userId = "user1";
        Pageable pageable = Pageable.unpaged();
        Wishlist wishlist = createWishlist();
        Page<Wishlist> wishlistPage = new PageImpl<>(List.of(wishlist));

        when(wishlistRepository.countByUserId(userId)).thenReturn(1L);
        when(wishlistRepository.findByUserId(userId, pageable)).thenReturn(wishlistPage);

        Page<Wishlist> result = wishlistService.getWishlistByUser(userId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("prod1", result.getContent().get(0).getProductId());
        verify(wishlistRepository).findByUserId(userId, pageable);
    }

    @Test
    void shouldRemoveFromWishlistSuccessfully() {
        String userId = "user1";
        String productId = "prod1";

        when(wishlistRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(true);
        when(wishlistRepository.countByUserId(userId)).thenReturn(0L);

        wishlistService.removeFromWishlist(userId, productId);

        verify(wishlistRepository).deleteByUserIdAndProductId(userId, productId);
    }

    @Test
    void shouldThrowProductNotFoundInWishlistException() {
        String userId = "user1";
        String productId = "prod1";

        when(wishlistRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(false);

        assertThrows(ProductNotFoundInWishlistException.class, () ->
                wishlistService.removeFromWishlist(userId, productId));
    }

    @Test
    void shouldReturnTrueIfProductIsInWishlist() {
        when(wishlistRepository.existsByUserIdAndProductId("user1", "prod1")).thenReturn(true);

        assertTrue(wishlistService.isProductInWishlist("user1", "prod1"));
    }

    @Test
    void shouldReturnFalseIfProductIsNotInWishlist() {
        when(wishlistRepository.existsByUserIdAndProductId("user1", "prod1")).thenReturn(false);

        assertFalse(wishlistService.isProductInWishlist("user1", "prod1"));
    }
}
