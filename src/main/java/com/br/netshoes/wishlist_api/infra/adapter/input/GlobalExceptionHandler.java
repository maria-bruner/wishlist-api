package com.br.netshoes.wishlist_api.infra.adapter.input;

import com.br.netshoes.wishlist_api.domain.exception.ProductAlreadyInWishlistException;
import com.br.netshoes.wishlist_api.domain.exception.ProductNotFoundInWishlistException;
import com.br.netshoes.wishlist_api.domain.exception.WishlistLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductAlreadyInWishlistException.class)
    public ResponseEntity<String> handleProductAlreadyInWishlistException(ProductAlreadyInWishlistException exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductNotFoundInWishlistException.class)
    public ResponseEntity<String> handleProductNotFoundInWishlistException(ProductNotFoundInWishlistException exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WishlistLimitExceededException.class)
    public ResponseEntity<String> handleWishlistLimitExceededException(WishlistLimitExceededException exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        String validationMessage = "Falha na validação: ";
        if (!errors.isEmpty()) {
            validationMessage += errors.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("; "));
        } else if (exception.getBindingResult().hasGlobalErrors()) {
            validationMessage += exception.getBindingResult().getGlobalErrors().get(0).getDefaultMessage();
        } else {
            validationMessage += "Dados da requisição inválidos.";
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", validationMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
    }
}