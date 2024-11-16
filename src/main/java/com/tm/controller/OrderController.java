package com.tm.controller;

import com.tm.dto.OrderRequestDTO;
import com.tm.dto.OrderResponseDTO;
import com.tm.dto.OrderUpdateRequest;
import com.tm.service.OrderService;
import com.tm.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // GET ORDERS
    @PreAuthorize("hasAuthority('Staff')")
    @GetMapping("/staff/orders")
    public ResponseEntity<Response<List<OrderResponseDTO>>> getOrders() {
        return orderService.getOrders();
    }

    // CREATE ORDER
    @PostMapping("/restaurant/order")
    public ResponseEntity<Response<Void>> createOrder(@RequestBody OrderRequestDTO request) {
        return orderService.createOrder(request);
    }

    // UPDATE ORDER STATUS
    @PreAuthorize("hasAuthority('Staff')")
    @PatchMapping("/staff/order/{id}")
    public ResponseEntity<Response<Void>> updateOrder(@PathVariable Long id, @ModelAttribute OrderUpdateRequest request) {
        return orderService.updateOrder(id, request);
    }

    // DELETE ORDER
    @PreAuthorize("hasAuthority('Staff')")
    @DeleteMapping("/staff/order/{id}")
    public ResponseEntity<Response<Void>> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}