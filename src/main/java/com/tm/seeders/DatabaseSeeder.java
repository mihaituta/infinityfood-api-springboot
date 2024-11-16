package com.tm.seeders;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {

    private final UserSeeder userSeeder;
    private final RestaurantSeeder restaurantSeeder;
    private final MenuSeeder menuSeeder;
    private final OrderSeeder orderSeeder;

    public DatabaseSeeder(UserSeeder userSeeder,
                          RestaurantSeeder restaurantSeeder,
                          MenuSeeder menuSeeder,
                          OrderSeeder orderSeeder) {
        this.userSeeder = userSeeder;
        this.restaurantSeeder = restaurantSeeder;
        this.menuSeeder = menuSeeder;
        this.orderSeeder = orderSeeder;
    }

    @PostConstruct
    public void seedDatabase() {
        // Run seeders in the required order
        userSeeder.seed();
        restaurantSeeder.seed();
        menuSeeder.seed();
        orderSeeder.seed();
    }
}
