package com.tm.service;

import com.tm.dto.MenuDetailsDTO;
import com.tm.dto.MenuRequest;
import com.tm.dto.UserResponseDTO;
import com.tm.model.Menu;
import com.tm.model.Restaurant;
import com.tm.repository.MenuRepository;
import com.tm.repository.RestaurantRepository;
import com.tm.util.CloudinaryService;
import com.tm.util.Response;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MenuService {

    private final AuthService authService;
    private final CloudinaryService cloudinaryService;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public MenuService(AuthService authService, CloudinaryService cloudinaryService,
                       RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.authService = authService;
        this.cloudinaryService = cloudinaryService;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    // GET MENUS
    public ResponseEntity<Response<Object>> getMenus() {
        try {
            UserResponseDTO loggedInUser = authService.getLoggedInUser();
            Restaurant restaurant = restaurantRepository.findByUserId(loggedInUser.getId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found for the logged-in user"));

            List<Menu> menus = menuRepository.findByRestaurantId(restaurant.getId());

            // Extract unique menu types and sort them based on a predefined order
            List<String> predefinedOrder = List.of("Starter", "Main Course", "Fastfood", "Pizza", "Dessert", "Drinks");
            List<String> types = menus.stream()
                    .map(Menu::getType)
                    .distinct()
                    .sorted(Comparator.comparingInt(predefinedOrder::indexOf))
                    .toList();

            List<MenuDetailsDTO> menuDTOs = menus.stream().map(menu -> new MenuDetailsDTO(
                    menu.getId(),
                    menu.getName(),
                    menu.getDescription(),
                    menu.getPrice(),
                    menu.getImage(),
                    menu.getType()
            )).toList();

            Map<String, Object> data = Map.of(
                    "menus", menuDTOs,
                    "types", types
            );

            return ResponseEntity.ok(new Response<>("success", "Menus retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error creating menu", e.getMessage()));
        }
    }

    // CREATE MENU
    public ResponseEntity<Response<Void>> createMenu(MenuRequest request) {
        try {
            UserResponseDTO loggedInUser = authService.getLoggedInUser();
            Restaurant restaurant = restaurantRepository.findByUserId(loggedInUser.getId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found for the logged-in user"));

            if(menuRepository.existsByNameAndRestaurantId(request.getName(), restaurant.getId())) {
                return ResponseEntity.ok(new Response<>("error", "A menu with this name already exists in the restaurant!", "nameTaken"));
            }

            Menu menu = new Menu();
            menu.setName(request.getName());
            menu.setDescription(request.getDescription());
            menu.setPrice(request.getPrice());
            menu.setType(request.getType());
            menu.setRestaurantId(restaurant.getId());
            menu.setImage(cloudinaryService.uploadImage(request.getImage(), restaurant.getSlug() + "/menu-images", "menu-" + UUID.randomUUID()));

            menuRepository.save(menu);

            return ResponseEntity.ok(new Response<>("success", "Menu created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error creating menu", e.getMessage()));
        }
    }

    // UPDATE MENU
    @Transactional
    public ResponseEntity<Response<Void>> updateMenu(Long id, MenuRequest request) {
        try {
            UserResponseDTO loggedInUser = authService.getLoggedInUser();

            Menu menu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));

            Restaurant restaurant = restaurantRepository.findByUserId(loggedInUser.getId()).orElseThrow(() -> new RuntimeException("Restaurant not found"));

            if(menuRepository.existsByNameAndRestaurantId(request.getName(), restaurant.getId())) {
                return ResponseEntity.ok(new Response<>("error", "A menu with this name already exists in the restaurant!", "nameTaken"));
            }

            // Check if the logged-in user has permission to update the menu (ensure they have access to this restaurant)
            if(!menu.getRestaurant().equals(restaurant)) {
                return ResponseEntity.status(403).body(new Response<>("error", "You don't have permission to update this menu"));
            }

            if(request.getName() != null) {
                menu.setName(request.getName());
            }

            if(request.getDescription() != null) {
                menu.setDescription(request.getDescription());
            }

            if(request.getPrice() != null) {
                menu.setPrice(request.getPrice());
            }

            if(request.getType() != null) {
                menu.setType(request.getType());
            }

            if(request.getImage() != null) {
                cloudinaryService.deleteImage(menu.getImage());
                menu.setImage(cloudinaryService.uploadImage(request.getImage(), restaurant.getSlug() + "/menu-images", "menu-" + UUID.randomUUID()));
            }

            menuRepository.save(menu);

            return ResponseEntity.ok(new Response<>("success", "Menu updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>("error", "Error updating menu", e.getMessage()));
        }
    }

    // DELETE MENU
    public ResponseEntity<Response<Void>> deleteMenu(Long id) {
        try {
            Menu menu = menuRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu not found"));

            UserResponseDTO loggedInUser = authService.getLoggedInUser();
            Restaurant restaurant = restaurantRepository.findByUserId(loggedInUser.getId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found for the logged-in user"));

            // Check if the store of the menu matches the logged-in user's store
            if(!menu.getRestaurant().getId().equals(restaurant.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new Response<>("error", "You don't have permission to delete this menu"));
            }


            cloudinaryService.deleteImage(menu.getImage());
            menuRepository.delete(menu);

            return ResponseEntity.ok(new Response<>("success", "Menu deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error deleting menu", e.getMessage()));
        }
    }
}