package com.tm.service;

import com.tm.model.Restaurant;
import com.tm.repository.RestaurantRepository;
import com.tm.repository.UserIdNameProjection;
import com.tm.repository.UserRepository;
import com.tm.security.Role;
import com.tm.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // GET RESTAURANTS COMPLETE
    public ResponseEntity<Response<Object>> getRestaurantsComplete(){
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>("error", "Failed to retrieve restaurants: ",e.getMessage()));
        }
    }

    // CREATE RESTAURANT
    public ResponseEntity<Response<String>> createRestaurant(String name, Long userId, String city, String previewDescription, MultipartFile previewImage, MultipartFile backgroundImage, MultipartFile logoImage, String contactText, String phone1, String phone2, String mail1, String mail2, String aboutText) throws Exception {
        if (restaurantRepository.existsRestaurantByName(name)) {
            return ResponseEntity.ok(new Response<>("error", "Restaurant already registered", "uniqueName"));
        }

        try {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(name);
            restaurant.setSlug(name.toLowerCase().replaceAll(" ", "-"));
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

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>("error", "Failed to create restaurant: " + e.getMessage()));
        }
    }
}
