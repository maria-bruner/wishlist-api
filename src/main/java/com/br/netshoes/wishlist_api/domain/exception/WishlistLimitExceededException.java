package com.br.netshoes.wishlist_api.domain.exception;

public class WishlistLimitExceededException extends RuntimeException {

    public WishlistLimitExceededException(String message) {
        super(message);
    }

    public WishlistLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}