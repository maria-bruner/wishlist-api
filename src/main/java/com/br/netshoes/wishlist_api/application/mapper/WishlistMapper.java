package com.br.netshoes.wishlist_api.application.mapper;

import com.br.netshoes.wishlist_api.application.dto.WishlistRequest;
import com.br.netshoes.wishlist_api.application.dto.WishlistResponse;
import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public Wishlist toDomain(WishlistRequest request) {
        return Wishlist.builder()
                .userId(request.userId())
                .productId(request.productId())
                .productName(request.productName())
                .productValue(request.productValue())
                .urlImage(request.urlImage())
                .build();
    }

    public WishlistResponse toResponse(Wishlist wishlist) {
        return new WishlistResponse(
                wishlist.getWishlistId(),
                wishlist.getUserId(),
                wishlist.getProductId(),
                wishlist.getProductName(),
                wishlist.getProductValue(),
                wishlist.getUrlImage()
        );
    }
}