package com.br.netshoes.wishlist_api.application.mapper;

import com.br.netshoes.wishlist_api.application.dto.WishlistRequest;
import com.br.netshoes.wishlist_api.application.dto.WishlistResponse;
import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WishlistMapper Behaviors")
class WishlistMapperTest {

    private WishlistMapper wishlistMapper;

    @BeforeEach
    void setUp() {
        wishlistMapper = new WishlistMapper();
    }

    @Test
    void shouldMapWishlistRequestToDomainSuccessfully() {
        WishlistRequest request = new WishlistRequest(
                "user123",
                "prod456",
                "Smartphone XPTO",
                new BigDecimal("1500.00"),
                "http://example.com/phone.jpg"
        );

        Wishlist domain = wishlistMapper.toDomain(request);

        assertThat(domain).isNotNull();
        assertThat(domain.getUserId()).isEqualTo(request.userId());
        assertThat(domain.getProductId()).isEqualTo(request.productId());
        assertThat(domain.getProductName()).isEqualTo(request.productName());
        assertThat(domain.getProductValue()).isEqualTo(request.productValue());
        assertThat(domain.getUrlImage()).isEqualTo(request.urlImage());
        assertThat(domain.getWishlistId()).isNull();
    }

    @Test
    void shouldMapDomainToWishlistResponseSuccessfully() {
        Wishlist domain = Wishlist.builder()
                .wishlistId("wish123abc")
                .userId("user123")
                .productId("prod456")
                .productName("Smartphone XPTO")
                .productValue(new BigDecimal("1500.00"))
                .urlImage("http://example.com/phone.jpg")
                .build();

        WishlistResponse response = wishlistMapper.toResponse(domain);

        assertThat(response).isNotNull();
        assertThat(response.wishlistId()).isEqualTo(domain.getWishlistId());
        assertThat(response.userId()).isEqualTo(domain.getUserId());
        assertThat(response.productId()).isEqualTo(domain.getProductId());
        assertThat(response.productName()).isEqualTo(domain.getProductName());
        assertThat(response.productValue()).isEqualTo(domain.getProductValue());
        assertThat(response.urlImage()).isEqualTo(domain.getUrlImage());
    }

    @Test
    void shouldHandleNullOptionalFieldsInRequest() {
        WishlistRequest request = new WishlistRequest(
                "user123",
                "prod456",
                "Product without image",
                new BigDecimal("50.00"),
                null
        );

        Wishlist domain = wishlistMapper.toDomain(request);

        assertThat(domain).isNotNull();
        assertThat(domain.getProductName()).isEqualTo("Product without image");
        assertThat(domain.getUrlImage()).isNull();
    }

    @Test
    void shouldHandleNullOptionalFieldsInDomain() {
        Wishlist domain = Wishlist.builder()
                .wishlistId("wish456def")
                .userId("user123")
                .productId("prod789")
                .productName("Another Product")
                .productValue(new BigDecimal("25.00"))
                .urlImage(null)
                .build();

        WishlistResponse response = wishlistMapper.toResponse(domain);

        assertThat(response).isNotNull();
        assertThat(response.productName()).isEqualTo("Another Product");
        assertThat(response.urlImage()).isNull();
    }
}