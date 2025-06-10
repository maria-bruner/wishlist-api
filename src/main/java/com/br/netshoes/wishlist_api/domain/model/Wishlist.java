package com.br.netshoes.wishlist_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "wishlist")
public class Wishlist {

    @Id
    private String wishlistId;

    private String userId;

    private String productId;

    private String productName;

    private BigDecimal productValue;

    private String urlImage;

}