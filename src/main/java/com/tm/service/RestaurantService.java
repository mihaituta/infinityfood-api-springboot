package com.tm.service;

import com.tm.dto.UpdateRestaurantRequest;
import com.tm.dto.UserResponseDTO;
import com.tm.model.Restaurant;
import com.tm.repository.RestaurantPreviewProjection;
import com.tm.repository.RestaurantRepository;
import com.tm.repository.UserIdNameProjection;
import com.tm.repository.UserRepository;
import com.tm.security.Role;
import com.tm.util.CloudinaryService;
import com.tm.util.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    private final CloudinaryService cloudinaryService;

    private final AuthService authService;
    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, CloudinaryService cloudinaryService, AuthService authService) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.authService = authService;
    }

    private String generateSlug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    // ----- PUBLIC -----
    // GET RESTAURANTS COMPLETE
    public ResponseEntity<Response<Object>> getRestaurantsComplete() {
        try {
            List<Restaurant> restaurants = restaurantRepository.findAll();
            List<UserIdNameProjection> staffUsers = userRepository.findUserByRoleId(Role.Staff);

            Map<String, Object> data = new HashMap<>();
            data.put("restaurants", restaurants);
            data.put("users", staffUsers);

            Response<Object> response = new Response<>("success", "Restaurants and Staff Users retrieved successfully");
            response.setData(data);

            //return ResponseEntity.ok(new Response<>("success", "Restaurants retrieved successfully", restaurants));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>("error", "Failed to retrieve restaurants: ", e.getMessage()));
        }
    }

    // GET RESTAURANTS PREVIEW
    public ResponseEntity<Response<Object>> getRestaurantsPreview() {
        try {
            List<RestaurantPreviewProjection> restaurantsPreview = restaurantRepository.findBy();

            List<String> sortedUniqueCities = restaurantsPreview.stream()
                    .map(RestaurantPreviewProjection::getCity).distinct().sorted().collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("restaurants", restaurantsPreview);
            data.put("cities", sortedUniqueCities);

            Response<Object> response = new Response<>("success", "Restaurant previews retrieved successfully");
            response.setData(data);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Failed to retrieve restaurant previews: ", e.getMessage()));
        }
    }

    // GET RESTAURANT BY SLUG
    public ResponseEntity<Response<Object>> getRestaurantComplete(String slug) {
        try {
            Restaurant restaurant = restaurantRepository.findBySlug(slug)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            // Get the menus for the restaurant
            //List<Menu> menus = menuRepository.findByRestaurantId(restaurant.getId());

            // Extract types from menus
            /*List<String> types = menus.stream()
                    .map(Menu::getType)
                    .distinct()
                    .collect(Collectors.toList());*/

            // Define the order of types
            List<String> order = Arrays.asList("Starter", "Main Course", "Fastfood", "Pizza", "Dessert", "Drinks");

            // Reorder types based on the predefined order
          /*  List<String> orderedTypes = types.stream()
                    .filter(order::contains)
                    .sorted(Comparator.comparingInt(order::indexOf))
                    .collect(Collectors.toList());*/

            // Prepare the response
            Map<String, Object> data = new HashMap<>();
            data.put("restaurant", restaurant);
            //data.put("types", orderedTypes);
            //data.put("menus", menus);

            Response<Object> response = new Response<>("success", "Restaurant retrieved successfully");
            response.setData(data);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Failed to retrieve restaurant: ", e.getMessage()));
        }
    }

    // ----- ADMIN ONLY -----
    // CREATE RESTAURANT
    public ResponseEntity<Response<String>> createRestaurant(String name, Long userId, String city, String previewDescription, MultipartFile previewImage, MultipartFile backgroundImage, MultipartFile logoImage, String contactText, String phone1, String phone2, String mail1, String mail2, String aboutText) {
        try {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(name);
            restaurant.setSlug(generateSlug(name));
            restaurant.setUserId(userId);
            restaurant.setCity(city);
            restaurant.setPreviewDescription(previewDescription);

            // Upload images to Cloudinary and set their URLs
            restaurant.setPreviewImage(cloudinaryService.uploadImage(previewImage, restaurant.getSlug() + "/restaurant-images", "previewImage"));
            restaurant.setBackgroundImage(cloudinaryService.uploadImage(backgroundImage, restaurant.getSlug() + "/restaurant-images", "backgroundImage"));
            restaurant.setLogoImage(cloudinaryService.uploadImage(logoImage, restaurant.getSlug() + "/restaurant-images", "logoImage"));

            restaurant.setContactText(contactText);
            restaurant.setPhone1(phone1);
            restaurant.setPhone2(phone2);
            restaurant.setMail1(mail1);
            restaurant.setMail2(mail2);
            restaurant.setAboutText(aboutText);

            restaurantRepository.save(restaurant);

            return ResponseEntity.ok(new Response<>("success", "Restaurant created successfully"));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok(new Response<>("error", "Restaurant already registered", "nameTaken"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>("error", "Failed to create restaurant: " + e.getMessage()));
        }
    }

    // UPDATE RESTAURANT
    public ResponseEntity<Response<Void>> updateRestaurant(Long id, UpdateRestaurantRequest request) {
        try {
            if (request.getName() != null && restaurantRepository.existsRestaurantByName(request.getName())) {
                return ResponseEntity.ok().body(new Response<>("error", "A restaurant with this name already exists!", "nameTaken"));
            }
            Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant not found"));

            updateRestaurantData(request, restaurant);
            restaurantRepository.save(restaurant);

            return ResponseEntity.ok(new Response<>("success", "Restaurant updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error updating restaurant: ", e.getMessage()));
        }
    }

    // DELETE RESTAURANT
    public ResponseEntity<Response<Void>> deleteRestaurant(Long id) {
        try {
            Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant not found"));

            String restaurantFolder = restaurant.getSlug();

            // Delete restaurant and menu images
            cloudinaryService.deleteAssetsByPrefix(restaurantFolder + "/restaurant-images");
            cloudinaryService.deleteAssetsByPrefix(restaurantFolder + "/menu-images");
            // Delete the restaurant folder itself
            cloudinaryService.deleteFolder(restaurantFolder);

            restaurantRepository.delete(restaurant);

            return ResponseEntity.ok(new Response<>("success", "Restaurant deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error deleting restaurant", e.getMessage()));
        }
    }

    // ----- STAFF ONLY -----

    // GET RESTAURANT WHERE LOGGED-IN USER IS A STAFF MEMBER
    public ResponseEntity<Response<Object>> getRestaurantByLoggedInUser(){
        try {
            UserResponseDTO loggedInUser = authService.getLoggedInUser();
            Optional<Restaurant> restaurantOptional = restaurantRepository.findByUserId(loggedInUser.getId());

            if (restaurantOptional.isPresent()) {
                Restaurant restaurant = restaurantOptional.get();
                return ResponseEntity.ok(new Response<>("success", "Restaurant found!", restaurant));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>("error", "Restaurant not found for the user"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>("error", "Error retrieving restaurant", e.getMessage()));
        }
    }

    // STAFF MEMBER UPDATE HIS RESTAURANT
    public ResponseEntity<Response<Void>> staffUpdateRestaurant(UpdateRestaurantRequest request) {
        try {
            if (request.getName() != null && restaurantRepository.existsRestaurantByName(request.getName())) {
                return ResponseEntity.ok().body(new Response<>("error", "A restaurant with this name already exists!", "nameTaken"));
            }

            UserResponseDTO loggedInUser = authService.getLoggedInUser();
            Restaurant restaurant = restaurantRepository.findByUserId(loggedInUser.getId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found for the logged-in staff user"));

            updateRestaurantData(request, restaurant);
            restaurantRepository.save(restaurant);

            return ResponseEntity.ok(new Response<>("success", "Restaurant updated successfully by staff"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error updating restaurant: ", e.getMessage()));
        }
    }

    private void updateRestaurantData(@NotNull UpdateRestaurantRequest request, Restaurant restaurant) throws IOException {
        if (request.getName() != null) {
            restaurant.setName(request.getName());
            restaurant.setSlug(generateSlug(request.getName()));
        }

        if (request.getUserId() != null) {
            restaurant.setUserId(request.getUserId());
        }

        if (request.getCity() != null) {
            restaurant.setCity(request.getCity());
        }

        if (request.getPreviewDescription() != null) {
            restaurant.setPreviewDescription(request.getPreviewDescription());
        }

        if (request.getPreviewImage() != null) {
            cloudinaryService.deleteImage(restaurant.getPreviewImage());
            restaurant.setPreviewImage(cloudinaryService.uploadImage(request.getPreviewImage(), restaurant.getSlug() + "/restaurant-images", "PreviewImage"));
        }

        if (request.getBackgroundImage() != null) {
            cloudinaryService.deleteImage(restaurant.getBackgroundImage());
            restaurant.setBackgroundImage(cloudinaryService.uploadImage(request.getBackgroundImage(), restaurant.getSlug() + "/restaurant-images", "BackgroundImage"));
        }

        if (request.getLogoImage() != null) {
            cloudinaryService.deleteImage(restaurant.getLogoImage());
            restaurant.setLogoImage(cloudinaryService.uploadImage(request.getLogoImage(), restaurant.getSlug() + "/restaurant-images", "LogoImage"));
        }

        if (request.getContactText() != null) {
            restaurant.setContactText(request.getContactText());
        }

        if (request.getPhone1() != null) {
            restaurant.setPhone1(request.getPhone1());
        }

        if (request.getPhone2() != null) {
            restaurant.setPhone2(request.getPhone2());
        }

        if (request.getMail1() != null) {
            restaurant.setMail1(request.getMail1());
        }

        if (request.getMail2() != null) {
            restaurant.setMail2(request.getMail2());
        }

        if (request.getAboutText() != null) {
            restaurant.setAboutText(request.getAboutText());
        }
    }
}