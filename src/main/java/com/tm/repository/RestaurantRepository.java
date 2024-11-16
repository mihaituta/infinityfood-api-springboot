package com.tm.repository;

import com.tm.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findBySlug(String slug);
    Optional<Restaurant> findByUserId(Long userId);
    boolean existsRestaurantByName(String name);
    List<RestaurantPreviewProjection> findBy();
}
