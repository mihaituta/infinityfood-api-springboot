package com.tm.repository;

import com.tm.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);
    List<Menu> findByRestaurantId(Long restaurantId);
}
