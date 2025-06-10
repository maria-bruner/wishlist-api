package com.br.netshoes.wishlist_api.domain.exception;

public class ProductAlreadyInWishlistException extends RuntimeException {

    public ProductAlreadyInWishlistException(String message) {
        super(message);
    }

    public ProductAlreadyInWishlistException(String message, Throwable cause) {
        super(message, cause);
    }
}