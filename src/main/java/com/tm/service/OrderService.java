package com.tm.service;

import com.tm.dto.OrderRequestDTO;
import com.tm.dto.OrderResponseDTO;
import com.tm.dto.OrderUpdateRequest;
import com.tm.dto.UserResponseDTO;
import com.tm.model.Order;
import com.tm.model.Restaurant;
import com.tm.repository.OrderRepository;
import com.tm.repository.RestaurantRepository;
import com.tm.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final AuthService authService;

    public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository, AuthService authService) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.authService = authService;
    }

    // GET ORDERS
    public ResponseEntity<Response<List<OrderResponseDTO>>> getOrders() {
        try {
            UserResponseDTO loggedInUser = authService.getLoggedInUser();
            Restaurant restaurant = restaurantRepository.findByUserId(loggedInUser.getId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found for the logged-in user"));
            List<Order> orders = orderRepository.findByRestaurantId(restaurant.getId());

            List<OrderResponseDTO> orderDTOs = orders.stream()
                    .map(order -> new OrderResponseDTO(
                            order.getId(),
                            order.getStatus(),
                            order.getTotalPrice(),
                            order.getMenus(),
                            order.getName(),
                            order.getPhone(),
                            order.getCity(),
                            order.getAddress(),
                            order.getHouseNr(),
                            order.getFloor(),
                            order.getApartment(),
                            order.getInformation()
                    ))
                    .toList();

            return ResponseEntity.ok(new Response<>("success", "Orders retrieved successfully", orderDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error retrieving orders", e.getMessage()));
        }
    }

    // CREATE ORDER
    public ResponseEntity<Response<Void>> createOrder(OrderRequestDTO request) {
        try {
            // Validate required fields
            if(request.getTotalPrice() == null || request.getMenus() == null ||
                    request.getName() == null || request.getPhone() == null ||
                    request.getCity() == null || request.getAddress() == null) {
                return ResponseEntity.badRequest().body(new Response<>("error", "Validation error: Missing required fields"));
            }

            // Create and populate the order
            Order order = new Order();
            order.setStatus(Order.STATUS_IN_PROGRESS);
            order.setTotalPrice(request.getTotalPrice());
            order.setMenus(request.getMenus());
            order.setName(request.getName());
            order.setPhone(request.getPhone());
            order.setCity(request.getCity());
            order.setAddress(request.getAddress());
            order.setRestaurantId(request.getRestaurantId());

            // Optional fields
            if(request.getHouseNr() != null)
                order.setHouseNr(request.getHouseNr());
            if(request.getFloor() != null)
                order.setFloor(request.getFloor());
            if(request.getApartment() != null)
                order.setApartment(request.getApartment());
            if(request.getInformation() != null)
                order.setInformation(request.getInformation());

            // Save the order
            orderRepository.save(order);

            return ResponseEntity.ok(new Response<>("success", "Order created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error creating order", e.getMessage()));
        }
    }

    // UPDATE ORDER STATUS
    public ResponseEntity<Response<Void>> updateOrder(Long orderId, OrderUpdateRequest request) {
        try {
            // Validate the status
            if(request.getStatus() == null) {
                return ResponseEntity.badRequest().body(new Response<>("error", "Status is required", "statusRequired"));
            }

            // Find the order by ID
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Update the status
            order.setStatus(request.getStatus());

            // Save the updated order
            orderRepository.save(order);

            return ResponseEntity.ok(new Response<>("success", "Order updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error updating order", e.getMessage()));
        }
    }

    // DELETE ORDER
    public ResponseEntity<Response<Void>> deleteOrder(Long orderId) {
        try {
            UserResponseDTO loggedInUser = authService.getLoggedInUser();
            Restaurant restaurant = restaurantRepository.findByUserId(loggedInUser.getId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found for the logged-in user"));
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Ensure the order belongs to the restaurant
            if (!order.getRestaurantId().equals(restaurant.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new Response<>("error", "You don't have permission to delete this order", "unauthorized"));
            }

            orderRepository.delete(order);

            return ResponseEntity.ok(new Response<>("success", "Order deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error deleting order", e.getMessage()));
        }
    }
}