package com.br.netshoes.wishlist_api.domain.exception;

public class ProductNotFoundInWishlistException extends RuntimeException {

    public ProductNotFoundInWishlistException(String message) {
        super(message);
    }

    public ProductNotFoundInWishlistException(String message, Throwable cause) {
        super(message, cause);
    }
}