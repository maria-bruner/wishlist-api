package com.br.netshoes.wishlist_api.infra.loader;

import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import com.br.netshoes.wishlist_api.infra.adapter.output.WishlistRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class WishlistDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(WishlistDataLoader.class);

    @Autowired
    private WishlistRepository wishlistRepository;

    @PostConstruct
    public void loadWishlistData() {
        if (wishlistRepository.count() == 0) {
            try (InputStream inputStream = new ClassPathResource("wishlist-data.json").getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                List<Wishlist> wishlists = mapper.readValue(inputStream, new TypeReference<List<Wishlist>>() {});
                wishlistRepository.saveAll(wishlists);
                System.out.println("Dados importados com sucesso!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Migração já realizada.");
        }
    }
}