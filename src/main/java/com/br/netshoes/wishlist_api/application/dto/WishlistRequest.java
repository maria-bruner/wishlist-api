package com.br.netshoes.wishlist_api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WishlistRequest(
        @NotBlank String userId,
        @NotBlank String productId,
        @NotBlank String productName,
        @NotNull BigDecimal productValue,
        String urlImage
) { }