package com.tm.controller;

import com.tm.dto.UpdateRestaurantRequest;
import com.tm.service.RestaurantService;
import com.tm.util.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Validated
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    // ----- PUBLIC ROUTES -----

    // GET RESTAURANTS COMPLETE
    @GetMapping("/restaurants-complete")
    public ResponseEntity<Response<Object>> getRestaurantsComplete(){
        return restaurantService.getRestaurantsComplete();
    }
    // GET RESTAURANTS PREVIEW
    @GetMapping("/restaurants")
    public ResponseEntity<Response<Object>> getRestaurantsPreview(){
        return restaurantService.getRestaurantsPreview();
    }

    // GET RESTAURANT BY SLUG
    @GetMapping("/restaurant-complete/{slug}")
    public ResponseEntity<Response<Object>> getRestaurantComplete(@PathVariable String slug){
        return restaurantService.getRestaurantComplete(slug);
    }

    // ----- ADMIN ONLY ROUTES -----

    // CREATE RESTAURANT
    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/admin/restaurant")
    public ResponseEntity<Response<String>> createRestaurant(
            @RequestParam String name,
            @RequestParam Long userId,
            @RequestParam String city,
            @RequestParam String previewDescription,
            @RequestParam MultipartFile previewImage,
            @RequestParam MultipartFile backgroundImage,
            @RequestParam MultipartFile logoImage,
            @RequestParam String contactText,
            @RequestParam String phone1,
            @RequestParam String phone2,
            @RequestParam String mail1,
            @RequestParam String mail2,
            @RequestParam String aboutText) throws Exception {

           return restaurantService.createRestaurant(
                    name, userId, city, previewDescription, previewImage,
                    backgroundImage, logoImage, contactText, phone1, phone2, mail1, mail2, aboutText
            );
    }

    // UPDATE RESTAURANT
    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/admin/restaurant/{id}")
    public ResponseEntity<Response<Void>> updateRestaurant(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateRestaurantRequest request) {
        return restaurantService.updateRestaurant(id, request);
    }

    // DELETE RESTAURANT
    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/admin/restaurant/{id}")
    public ResponseEntity<Response<Void>> deleteRestaurant(@PathVariable Long id) {
        return restaurantService.deleteRestaurant(id);
    }

    // ----- STAFF ONLY ROUTES -----
    // GET RESTAURANT WHERE LOGGED-IN USER IS A STAFF MEMBER
    @PreAuthorize("hasAuthority('Staff')")
    @GetMapping("/staff/restaurant")
    public ResponseEntity<Response<Object>> getRestaurantByLoggedInUser() {
        return restaurantService.getRestaurantByLoggedInUser();
    }

    // STAFF MEMBER UPDATE HIS RESTAURANT
    @PreAuthorize("hasAuthority('Staff')")
    @PatchMapping("/staff/restaurant")
    public ResponseEntity<Response<Void>> staffUpdateRestaurant(@ModelAttribute UpdateRestaurantRequest request) {
        return restaurantService.staffUpdateRestaurant(request);
    }
}
