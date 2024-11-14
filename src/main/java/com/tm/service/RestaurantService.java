package com.tm.service;

import com.tm.dto.UserResponseDTO;
import com.tm.model.Restaurant;
import com.tm.repository.RestaurantPreviewProjection;
import com.tm.repository.RestaurantRepository;
import com.tm.repository.UserIdNameProjection;
import com.tm.repository.UserRepository;
import com.tm.security.Role;
import com.tm.util.CloudinaryService;
import com.tm.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    private final AuthService authService;
    public RestaurantService(AuthService authService) {
        this.authService = authService;
    }

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

    // GET RESTAURANT BY ID
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
    // CREATE RESTAURANT
    public ResponseEntity<Response<String>> createRestaurant(String name, Long user_id, String city, String previewDescription, MultipartFile previewImage, MultipartFile backgroundImage, MultipartFile logoImage, String contactText, String phone1, String phone2, String mail1, String mail2, String aboutText) throws Exception {
        try {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(name);
            restaurant.setSlug(name.toLowerCase().replaceAll(" ", "-"));
            restaurant.setUserId(user_id);
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
            // Handle the unique constraint violation (duplicate name or slug)
            return ResponseEntity.ok(new Response<>("error", "Restaurant already registered", "nameTaken"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>("error", "Failed to create restaurant: " + e.getMessage()));
        }
    }

    // UPDATE RESTAURANT

    // STAFF MEMBER UPDATE HIS RESTAURANT

    // DELETE RESTAURANT
}
