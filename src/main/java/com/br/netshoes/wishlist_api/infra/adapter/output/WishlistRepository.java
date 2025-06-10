package com.br.netshoes.wishlist_api.infra.adapter.output;

import com.br.netshoes.wishlist_api.domain.model.Wishlist;
import com.br.netshoes.wishlist_api.domain.port.output.WishlistRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String>, WishlistRepositoryPort {

    Page<Wishlist> findByUserId(String userId, Pageable pageable);

    boolean existsByUserIdAndProductId(String userId, String productId);

    void deleteByUserIdAndProductId(String userId, String productId);

    long countByUserId(String userId);
}