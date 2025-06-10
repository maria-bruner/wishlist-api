package com.br.netshoes.wishlist_api.infra.adapter.input;

import com.br.netshoes.wishlist_api.application.dto.WishlistRequest;
import com.br.netshoes.wishlist_api.application.dto.WishlistResponse;
import com.br.netshoes.wishlist_api.application.mapper.WishlistMapper;
import com.br.netshoes.wishlist_api.domain.exception.ProductAlreadyInWishlistException;
import com.br.netshoes.wishlist_api.domain.exception.ProductNotFoundInWishlistException;
import com.br.netshoes.wishlist_api.domain.exception.WishlistLimitExceededException;
import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import com.br.netshoes.wishlist_api.domain.port.input.WishlistServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishlistServicePort wishlistService;

    @MockitoBean
    private WishlistMapper mapper;

    private Wishlist sampleWishlist;
    private WishlistResponse sampleResponse;
    private WishlistRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleWishlist = Wishlist.builder()
                .wishlistId("w1")
                .userId("user1")
                .productId("prod1")
                .productName("Produto 1")
                .productValue(new BigDecimal("99.90"))
                .urlImage("http://img.com/prod1.jpg")
                .build();

        sampleRequest = new WishlistRequest(
                "user1",
                "prod1",
                "Produto 1",
                new BigDecimal("99.90"),
                "http://img.com/prod1.jpg"
        );

        sampleResponse = new WishlistResponse(
                "w1",
                "user1",
                "prod1",
                "Produto 1",
                new BigDecimal("99.90"),
                "http://img.com/prod1.jpg"
        );
    }

    @Test
    void addDup() throws Exception {
        when(mapper.toDomain(sampleRequest)).thenReturn(sampleWishlist);
        doThrow(new ProductAlreadyInWishlistException("Produto já na wishlist"))
                .when(wishlistService).addToWishlist(sampleWishlist);

        mockMvc.perform(post("/api/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sampleRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Produto já na wishlist"));
    }

    @Test
    void addFull() throws Exception {
        when(mapper.toDomain(sampleRequest)).thenReturn(sampleWishlist);
        doThrow(new WishlistLimitExceededException("Wishlist cheia"))
                .when(wishlistService).addToWishlist(sampleWishlist);

        mockMvc.perform(post("/api/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sampleRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Wishlist cheia"));
    }

    @Test
    void removeNotFound() throws Exception {
        doThrow(new ProductNotFoundInWishlistException("Produto não encontrado"))
                .when(wishlistService).removeFromWishlist("user1", "prod1");

        mockMvc.perform(delete("/api/wishlist/user1/prod1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Produto não encontrado"));
    }
}