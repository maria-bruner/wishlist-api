package com.br.netshoes.wishlist_api.application.dto;

import java.math.BigDecimal;

public record WishlistResponse(
        String wishlistId,
        String userId,
        String productId,
        String productName,
        BigDecimal productValue,
        String urlImage
) { }