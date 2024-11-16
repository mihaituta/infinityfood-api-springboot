package com.tm.seeders;

import com.tm.model.Order;
import com.tm.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderSeeder implements CommandLineRunner {

    private final OrderRepository orderRepository;

    public OrderSeeder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // First order
        Order order1 = new Order();
        order1.setStatus(1);
        order1.setTotalPrice(223.50);
        order1.setMenus("1x Coca cola 330ml can, 2x Crispy Burger, 2x Pizza Diavola, 1x Cheese pancakes with raspberry filling, 1x Breaded zucchini with garlic sauce");
        order1.setName("John");
        order1.setPhone("0834759834");
        order1.setCity("Bucharest");
        order1.setAddress("Example");
        order1.setHouseNr("52");
        order1.setFloor("4");
        order1.setApartment("6");
        order1.setInformation("No onions in burgers please");
        order1.setRestaurantId(1L);

        orderRepository.save(order1);

        // Second order
        Order order2 = new Order();
        order2.setStatus(0);
        order2.setTotalPrice(345.44);
        order2.setMenus("2x Chiken breast on stick, 2x Dark cake, 2x Apple pie, 1x Strongbow Gold Apple 330ml, 1x Water Bucovina 500ml");
        order2.setName("Demo name");
        order2.setPhone("3425453453");
        order2.setCity("Bucharest");
        order2.setAddress("Example address");
        order2.setHouseNr("22");
        order2.setFloor("2");
        order2.setApartment("4");
        order2.setInformation("");
        order2.setRestaurantId(1L);

        orderRepository.save(order2);
    }
}
